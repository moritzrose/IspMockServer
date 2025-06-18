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
@RequestMapping("/sses")
public class SseController {

    public static final Map<String, CustomSseEmitter> emitters = new HashMap<>();
    //private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @GetMapping("/{sessionId}")
    public SseEmitter connect(@PathVariable String sessionId) {
        final CustomSseEmitter emitter = new CustomSseEmitter(0L);

        emitter.onCompletion(() -> {
            emitter.complete();
            emitters.remove(sessionId);
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(sessionId);
        });

        emitter.onError((e) -> {
            emitter.completeWithError(e);
            emitters.remove(sessionId);
        });

        emitters.put(sessionId, emitter);
        return emitter;
    }
}
