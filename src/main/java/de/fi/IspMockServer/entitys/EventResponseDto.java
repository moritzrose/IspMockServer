package de.fi.IspMockServer.entitys;

public class EventResponseDto {
    String message;
    String retcode;

    public EventResponseDto(String message, String retcode) {
        this.message = message;
        this.retcode = retcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }
}
