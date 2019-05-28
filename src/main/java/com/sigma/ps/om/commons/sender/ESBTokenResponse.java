package com.sigma.ps.om.commons.sender;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ESBTokenResponse {
    @JsonCreator
    public ESBTokenResponse(@JsonProperty("access_token") String access_token, @JsonProperty("token_type") String token_type, @JsonProperty("refresh_token") String refresh_token, @JsonProperty("expires_in") long expires_in, @JsonProperty("scope") String scope, @JsonProperty("jti") String jti){
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.scope = scope;
        this.jti = jti;
    }
    public final String access_token;

    public final String token_type;

    public final String refresh_token;

    public final long expires_in;

    public final String scope;

    public final String jti;
    public String getAccessToken() {
        return access_token;
    }
    public long getExpiresIn() {
        return expires_in;
    }
    public String getJti() {
        return jti;
    }
    public String getRefreshToken() {
        return refresh_token;
    }
    public String getScope() {
        return scope;
    }
    public String getTokenType() {
        return token_type;
    }
}
