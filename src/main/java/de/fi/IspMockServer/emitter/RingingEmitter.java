package de.fi.IspMockServer.emitter;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class RingingEmitter extends SseEmitter {

    public RingingEmitter(Long timeout) {
        super(timeout);
    }

    public void emit() throws IOException {
        //data leer aber notwendig, da sonst hx-trigger: sse:ringing nicht funktioniert
        send(SseEmitter.event().name("ringing").data(""));
    }
}
