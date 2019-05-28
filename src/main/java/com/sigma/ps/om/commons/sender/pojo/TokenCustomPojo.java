package com.sigma.ps.om.commons.sender.pojo;

import com.sigma.ps.om.commons.sender.ESBTokenResponse;

import java.util.Calendar;
import java.util.Date;

public class TokenCustomPojo {

    private ESBTokenResponse response;
    private Date             date;

    public Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public Date getDate() {
        return date;
    }

    public ESBTokenResponse getResponse() {
        return response;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setResponse(ESBTokenResponse response) {
        this.response = response;
    }

}
