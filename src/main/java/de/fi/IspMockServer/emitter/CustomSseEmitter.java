package de.fi.IspMockServer.emitter;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class CustomSseEmitter extends SseEmitter {

    public CustomSseEmitter(Long timeout) {
        super(timeout);
    }

    public void emit(String event, String data) throws IOException {
        send(SseEmitter.event().name(event).data(data));
    }
}
