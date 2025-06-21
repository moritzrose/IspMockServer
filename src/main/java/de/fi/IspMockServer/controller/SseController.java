package de.fi.IspMockServer.controller;

import de.fi.IspMockServer.emitter.CustomSseEmitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sse-stream")
public class SseController {

    public static final Map<String, CustomSseEmitter> emitters = new HashMap<>();

    @GetMapping("/{agentId}")
    public SseEmitter connect(@PathVariable String agentId) {
        final CustomSseEmitter emitter = new CustomSseEmitter(0L);

        emitter.onCompletion(() -> {
            emitter.complete();
            emitters.remove(agentId);
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(agentId);
        });

        emitter.onError((e) -> {
            emitter.completeWithError(e);
            emitters.remove(agentId);
        });

        emitters.put(agentId, emitter);
        return emitter;
    }
}
