package de.fi.IspMockServer.entitys;

public class Content {
    private String callId;
    private String caller;
    private String called;
    private String feature;
    private String mediaType;
    private String mediaAbility;
    private String otherPhoneWorkno;
    private String otherPhone;
    private String type;
    private String transfer;
    private String transform;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getTransform() {
        return transform;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getMediaAbility() {
        return mediaAbility;
    }

    public void setMediaAbility(String mediaAbility) {
        this.mediaAbility = mediaAbility;
    }

    public String getOtherPhoneWorkno() {
        return otherPhoneWorkno;
    }

    public void setOtherPhoneWorkno(String otherPhoneWorkno) {
        this.otherPhoneWorkno = otherPhoneWorkno;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
