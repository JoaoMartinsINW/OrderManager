package com.sigma.ps.om.commons.rest;

import com.sigma.om.cmn.CommonUtils;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RESTClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTClient.class);

    /**
     * @param acceptContentType e.g. application/json for response ContentType
     * @param endpointPath endpoint url
     * @return WebClient
     */
    private static WebClient getWebClient(String acceptContentType, final String endpointPath) {
        final JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setAddress(endpointPath);
        bean.getInInterceptors().add(new LoggingInInterceptor());
        bean.getOutInterceptors().add(new LoggingOutInterceptor());
        bean.getInFaultInterceptors().add(new LoggingInInterceptor());
        bean.getOutFaultInterceptors().add(new LoggingOutInterceptor());

        final WebClient client = bean.createWebClient();
        if (!CommonUtils.isEmpty(acceptContentType)) {
            client.accept(acceptContentType);
        }
        final HTTPConduit httpConduit = WebClient.getConfig(client).getHttpConduit();

        /**
         * let cxf.xml handle it
         * httpConduit.getClient().setConnectionTimeout(catSvcCfg.getConnectionTimeout() * ONE_Min);
         * httpConduit.getClient().setReceiveTimeout(catSvcCfg.getReadTimeout() * ONE_Min);
         */
        // log default values
        LOGGER.debug("CXF ConnectionTimeout  : {}", httpConduit.getClient().getConnectionTimeout());
        LOGGER.debug("CXF ReceiveTimeout  : {}", httpConduit.getClient().getReceiveTimeout());
        return client;
    }

    /**
     * HTTP delete method call
     *
     * @param acceptContentType
     * @param endpoint
     * @param headers
     * @param cookies
     * @return Response object
     */
    public static Response delete(String acceptContentType, final String endpoint, Map<String, String> headers,
            List<Cookie> cookies) {
        final WebClient client = WebClient.create(endpoint);
        if (cookies != null && !cookies.isEmpty()) {
            for (final Cookie cookie : cookies) {
                LOGGER.debug("cookie name : " + cookie.getName() + " value: " + cookie.getValue());
                client.cookie(cookie);
            }
        }
        if (!CommonUtils.isEmpty(headers)) {
            headers.forEach((key, value) -> {
                client.header(key, value);
            });
        }
        if (!CommonUtils.isEmpty(acceptContentType)) {
            client.accept(acceptContentType);
        }
        LOGGER.debug("headers {}", headers);
        LOGGER.debug("endpoint url {}", endpoint);
        WebClient.getConfig(client).getRequestContext().put("use.async.http.conduit", Boolean.TRUE);
        final Response response = client.delete();
        LOGGER.info("Response status {}", response.getStatus());
        return response;
    }

    /**
     * @param acceptContentType e.g. application/json for response ContentType
     * @param endpoint endpoint url
     * @param headers request headers
     * @param cookies cookies
     * @return Response
     */
    public static Response get(String acceptContentType, final String endpoint, Map<String, String> headers,
            List<Cookie> cookies) {
        final WebClient client = WebClient.create(endpoint);
        if (cookies != null && !cookies.isEmpty()) {
            for (final Cookie cookie : cookies) {
                LOGGER.debug("cookie name : " + cookie.getName() + " value: " + cookie.getValue());
                client.cookie(cookie);
            }
        }
        if (!CommonUtils.isEmpty(headers)) {
            headers.forEach((key, value) -> {
                client.header(key, value);
            });
        }
        if (!CommonUtils.isEmpty(acceptContentType)) {
            client.accept(acceptContentType);
        }
        LOGGER.debug("headers {}", headers);
        LOGGER.debug("endpoint url {}", endpoint);
        WebClient.getConfig(client).getRequestContext().put("use.async.http.conduit", Boolean.TRUE);
        final Response response = client.get();
        LOGGER.info("Response status {}", response.getStatus());
        return response;
    }

    /**
     * @param form for e.g. Content-Type, application/x-www-form-urlencoded type
     * @param acceptContentType e.g. application/json for response ContentType
     * @param headers request headers
     * @param endpoint enpoint url
     * @return Response
     */
    public static Response post(Form form, String acceptContentType, Map<String, String> headers,
            String endpoint) {
        final WebClient client = getWebClient(acceptContentType, endpoint);
        if (!CommonUtils.isEmpty(headers)) {
            headers.forEach((key, value) -> {
                client.header(key, value);
            });
        } else {
            LOGGER.info("Headers parameters are empty : {} ", headers);
        }
        LOGGER.info("sending form to endpoint url {}", endpoint);
        final Response response = client.post(form);
        LOGGER.info("Response status {}", response.getStatus());
        return response;
    }

    /**
     * @param payload request
     * @param acceptContentType e.g. application/json for response ContentType
     * @param headers custom header parameters
     * @param endpoint endpoint url
     * @return Response
     */
    public static Response post(String payload, String acceptContentType, Map<String, String> headers,
            String endpoint) {
        final WebClient client = getWebClient(acceptContentType, endpoint);
        if (!CommonUtils.isEmpty(headers)) {
            headers.forEach((key, value) -> {
                client.header(key, value);
            });
        } else {
            LOGGER.info("Headers parameters are empty : {} ", headers);
        }
        LOGGER.info("sending payload to endpoint url {}", endpoint);
        final Response response = client.post(payload);
        LOGGER.info("Response status {}", response.getStatus());
        return response;
    }

}
