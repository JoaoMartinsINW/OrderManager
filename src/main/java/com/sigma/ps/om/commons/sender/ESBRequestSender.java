package com.sigma.ps.om.commons.sender;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.om.cmn.CommonUtils;
import com.sigma.om.cmn.JsonUtils;
import com.sigma.om.sdk.soi.framework.SoiContext;
import com.sigma.om.sdk.soi.interaction.IMExternalInteraction;
import com.sigma.om.soi.framework.SoiConfig;
import com.sigma.om.soi.framework.exception.SoiRuntimeException;
import com.sigma.om.soi.interaction.ExternalResponseImpl;
import com.sigma.ps.om.commons.envcfg.EnvironmentUtil;
import com.sigma.ps.om.commons.http.HttpDeleteWithBody;
import com.sigma.ps.om.commons.rest.RESTClient;
import com.sigma.ps.om.commons.sender.pojo.TokenCustomPojo;

/**
 * The Class ESBRequestSender.
 *
 */
public final class ESBRequestSender implements Processor {

    public ESBRequestSender() {
    }

    /**
     * Instantiates a new instance.
     */
    private static Map<String, TokenCustomPojo> token                       = new ConcurrentHashMap<>();

    /** The Constant LOGGER. */
    private static final Logger                 LOGGER                      = LoggerFactory
            .getLogger(ESBRequestSender.class);
    /** ESB */
    private static final String                 ESB                         = "ESB";
    /** DEFAULT */
    private static final String                 DEFAULT                     = "DEFAULT";
    /** userName for enviromentCFG */
    private static final String                 USERNAME                    = "userName";
    /** username for payload */
    private static final String                 USERNAME_PO                 = "username";
    /** password */
    private static final String                 PASSWORD                    = "password";
    /** tokenUrl */
    private static final String                 TOKENURL                    = "tokenUrl";
    /** authPass */
    private static final String                 AUTHPASS                    = "authPass";
    /** Content-Type value for token call */
    private static final String                 APP_X_WWW_FORM_URL_CONCODED = "application/x-www-form-urlencoded";
    /** Content-Type value for token call */
    private static final String                 APP_JSON                    = "application/json";
    /** CONTENT_TYPE */
    private static final String                 CONTENT_TYPE                = "Content-Type";
    /** GRANT_TYPE **/
    private static final String                 GRANT_TYPE                  = "grant_type";
    /** AUTHORIZATION **/
    private static final String                 AUTHORIZATION               = "Authorization";
    /** BEARER */
    private static final String                 BEARER                      = "Bearer";
    /** REQUEST_URL */
    private static final String                 REQUEST_URL                 = "requestUrl";
    /** METHOD */
    private static final String                 METHOD                      = "method";
    /** ACCEPT CONTENT TYPE */
    private static final String                 ACCEPT_CONTENT_TYPE         = "acceptContentType";
    /** GET */
    private static final String                 GET                         = "GET";
    /** DELETE */
    private static final String                 DELETE                      = "DELETE";
    private static final String                 DELETE_WITH_BODY            = "DELETE_WITH_BODY";
    private static final String                 Stubbed                     = "Stubbed";
    private static final String                 TOKEN                       = "token";
    private static final String                 SKIP_TX                     = "skipTransaction";

    private static boolean checkESBCallRequired() {
        if (token.containsKey(TOKEN)) {
            final TokenCustomPojo tokenCustomPojo = token.get(TOKEN);
            if (tokenCustomPojo.getResponse().getExpiresIn() > getTimeDiff(tokenCustomPojo))
                return false;
        }
        return true;
    }

    /**
     * Process response.
     *
     * @param response
     * @throws SoiRuntimeException, Caller should handle and set FAILED status for activity
     */
    private static String processResponse(final Response response) throws SoiRuntimeException {
        LOGGER.info("Response Status :{}", response.getStatus());
        final InputStream iStream = (InputStream) response.getEntity();
        final StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(iStream, writer, Charset.defaultCharset());
        } catch (final IOException e) {
            throw new SoiRuntimeException(DEFAULT, e);
        }
        if (response.getStatus() != 200) {
            final String responseString = "Error Code : " + response.getStatus() + " Error Reason : "
                    + writer.toString();
            LOGGER.error("Exception {}", responseString);
            throw new SoiRuntimeException(DEFAULT, responseString);
        } else {
            LOGGER.debug("Response details {}", writer.toString());
            ESBTokenResponse tokenResponse;
            try {
                tokenResponse = new ObjectMapper().readValue(writer.toString(), ESBTokenResponse.class);
            } catch (final JsonParseException e) {
                LOGGER.error("JsonParseException {}", e);
                throw new SoiRuntimeException(DEFAULT, e);
            } catch (final JsonMappingException e) {
                LOGGER.error("JsonMappingException {}", e);
                throw new SoiRuntimeException(DEFAULT, e);
            } catch (final IOException e) {
                LOGGER.error("IOException {}", e);
                throw new SoiRuntimeException(DEFAULT, e);
            }
            LOGGER.debug(" tokenResponse : {} ", JsonUtils.toString(tokenResponse));
            final TokenCustomPojo tokenCustomPojo = new TokenCustomPojo();
            final Calendar calendarDateOne = Calendar.getInstance();
            tokenCustomPojo.setDate(calendarDateOne.getTime());
            tokenCustomPojo.setResponse(tokenResponse);
            token.put(TOKEN, tokenCustomPojo);
            return tokenResponse.getAccessToken();
        }
    }

    /**
     * Get ESB Token.
     */
    public static String getESBToken(String required) throws Exception {
        // Deployment must have configuration of ESB as Fulfillment system in envcfg
        if (checkESBCallRequired() || required.equals(ESB)) {
            synchronized (ESBRequestSender.class) {
                if (checkESBCallRequired() || required.equals(ESB)) {
                    LOGGER.info(" ESB call is required ");
                    final Map<String, String> esbCfg = EnvironmentUtil.getEnvConfig(ESB);
                    if (CommonUtils.isEmpty(esbCfg.get(TOKENURL))) {
                        throw new SoiRuntimeException("TOKEN URL is not configured or ESB envcfg is missing");
                    }

                    final Form form = new Form();
                    form.param(GRANT_TYPE, PASSWORD);
                    form.param(USERNAME_PO, esbCfg.get(USERNAME));
                    form.param(PASSWORD, esbCfg.get(PASSWORD));

                    final Map<String, String> headers = new HashMap<String, String>();
                    headers.put(CONTENT_TYPE, APP_X_WWW_FORM_URL_CONCODED);
                    final String authorizationPass = "Basic "
                            + DatatypeConverter.printBase64Binary(esbCfg.get(AUTHPASS).getBytes());
                    headers.put(AUTHORIZATION, authorizationPass);

                    final Response response = RESTClient.post(form, APP_JSON, headers, esbCfg.get(TOKENURL));
                    return processResponse(response);
                }
            }

        }
        LOGGER.info(" ESB call is not required ");
        return token.get(TOKEN).getResponse().getAccessToken();

    }

    public static long getTimeDiff(TokenCustomPojo pojo) {

        long timeDiff = Math.abs(pojo.getDate().getTime() - pojo.getCurrentDate().getTime());
        timeDiff = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
        System.out.println(timeDiff);
        return timeDiff;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.info(" In ESBRequestSender process method ");
        String token = getESBToken("false");
        final SoiContext soiContext = SoiConfig.getSoIContextFromInBody(exchange);
        if (soiContext == null) {
            throw new SoiRuntimeException(DEFAULT, "SoIContext CANNOT be null");
        }

        String requestBody = "";
        for (final IMExternalInteraction extInteraction : soiContext.getSoiExternalInteraction()) {
            requestBody = extInteraction.getRequest();
            LOGGER.info(" Request inside the ESBRequestSender {} ", requestBody);
        }

        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(AUTHORIZATION, new StringBuilder(BEARER).append(" ").append(token).toString());
        if (exchange.getIn().getHeader(Stubbed) != null) {
            headers.put("Stubbed", (String) exchange.getIn().getHeader(Stubbed));
        }
        final String requestUrl = (String) exchange.getIn().getHeader(REQUEST_URL);
        LOGGER.info("requestUrl {}", requestUrl);

        String acceptContentType;
        if (StringUtils.isEmpty((String) exchange.getIn().getHeader(ACCEPT_CONTENT_TYPE))) {
            acceptContentType = APP_JSON;
        } else {
            acceptContentType = (String) exchange.getIn().getHeader(ACCEPT_CONTENT_TYPE);
        }

        Response response;
        if (!exchange.getIn().getHeaders().containsKey(SKIP_TX)) {
            if (StringUtils.equalsIgnoreCase(GET, (String) exchange.getIn().getHeader(METHOD))) {
                // do not add content type for GET method call
                response = RESTClient.get(acceptContentType, requestUrl, headers, null);

                if (response.getStatus() == 401) {
                    token = getESBToken(ESB);
                    headers.put(AUTHORIZATION,
                            new StringBuilder(BEARER).append(" ").append(token).toString());

                    response = RESTClient.get(acceptContentType, requestUrl, headers, null);
                }
            } else if (StringUtils.equalsIgnoreCase(DELETE, (String) exchange.getIn().getHeader(METHOD))) {
                // do not add content type for DELETE method call
                response = RESTClient.delete(acceptContentType, requestUrl, headers, null);

                if (response.getStatus() == 401) {
                    token = getESBToken(ESB);
                    headers.put(AUTHORIZATION,
                            new StringBuilder(BEARER).append(" ").append(token).toString());

                    response = RESTClient.delete(acceptContentType, requestUrl, headers, null);
                }
            } else if (StringUtils.equalsIgnoreCase(DELETE_WITH_BODY,
                    (String) exchange.getIn().getHeader(METHOD))) {
                // response = RESTClient.delete(acceptContentType, requestUrl, headers, null);
                response = HttpDeleteWithBody.sendDelete(requestUrl, requestBody, headers);

                if (response.getStatus() == 401) {
                    token = getESBToken(ESB);
                    headers.put(AUTHORIZATION,
                            new StringBuilder(BEARER).append(" ").append(token).toString());
                    response = HttpDeleteWithBody.sendDelete(requestUrl, requestBody, headers);
                }
            } else {
                headers.put(CONTENT_TYPE, APP_JSON);
                response = RESTClient.post(requestBody, acceptContentType, headers, requestUrl);

                if (response.getStatus() == 401) {
                    token = getESBToken(ESB);
                    headers.put(AUTHORIZATION,
                            new StringBuilder(BEARER).append(" ").append(token).toString());
                    response = RESTClient.post(requestBody, acceptContentType, headers, requestUrl);
                }
            }

        } else {
            LOGGER.info("This transaction is skipped");
            if(requestBody.isEmpty()){
                requestBody="Request Payload is empty";
            }
            response = Response.ok()
                    .entity(new ReaderInputStream(new StringReader(requestBody), StandardCharsets.UTF_8))
                    .header(SKIP_TX, Boolean.TRUE).build();
        }

        ExternalResponseImpl res = new ExternalResponseImpl();
        LOGGER.info(" response code : {} ", response.getStatus());
        res.setResponse(response);
        res.setExtInteractionId(soiContext.getSoiExternalInteraction().get(0).getId());
        soiContext.getSoiExternalInteraction().get(0).addResponse(res);
        exchange.getIn().setBody(soiContext);
    }
}
