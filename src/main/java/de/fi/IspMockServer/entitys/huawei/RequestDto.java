package de.fi.IspMockServer.entitys.huawei;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestDto {
    String password;
    String phonenum;
    String callBackUri;
    String serviceToken;
    String status;
    boolean autoanswer;

    public boolean getAutoanswer() {
        return autoanswer;
    }

    public void setAutoanswer(boolean autoanswer) {
        this.autoanswer = autoanswer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getCallBackUri() {
        return callBackUri;
    }

    public void setCallBackUri(String callBackUri) {
        this.callBackUri = callBackUri;
    }

    public String getServiceToken() {
        return serviceToken;
    }

    public void setServiceToken(String serviceToken) {
        this.serviceToken = serviceToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
