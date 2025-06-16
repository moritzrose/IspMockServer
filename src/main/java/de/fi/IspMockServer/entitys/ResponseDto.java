package de.fi.IspMockServer.entitys;

import java.util.List;
import java.util.Map;

public class ResponseDto {
    Map<String, List<String>> header;
    String body;

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
