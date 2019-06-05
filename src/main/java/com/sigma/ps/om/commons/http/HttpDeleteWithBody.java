package com.sigma.ps.om.commons.http;

import com.sigma.om.cmn.CommonUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public HttpDeleteWithBody() {
        super();
    }

    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    private static final Logger LOGGER      = LoggerFactory.getLogger(HttpDeleteWithBody.class);
    public static final String  METHOD_NAME = "DELETE";

    public static Response sendDelete(String URL, String payload, Map<String, String> headers) throws IOException {
        final CloseableHttpClient httpclient = HttpClients.createDefault();

        LOGGER.info("Payload : {}", payload);
        final HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(URL);
        final StringEntity input = new StringEntity(payload, ContentType.APPLICATION_JSON);

        if (!CommonUtils.isEmpty(headers)) {
            headers.forEach((key, value) -> {
                httpDelete.addHeader(key, value);
            });
        }

        httpDelete.setEntity(input);
        LOGGER.info("Http Delete : {}", httpDelete);
        final Header requestHeaders[] = httpDelete.getAllHeaders();
        LOGGER.info("requestHeaders: {}", requestHeaders[0]);

        final CloseableHttpResponse response = httpclient.execute(httpDelete);

        final ResponseBuilder responseBuilder = new ResponseBuilderImpl().status(response.getStatusLine().getStatusCode());
        responseBuilder.entity(response.getEntity());
        httpclient.close();
        LOGGER.info("Status Code : {}", response.getStatusLine().getStatusCode());
        LOGGER.info("Response Entity : {}", response.getEntity());
        return responseBuilder.build();
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
