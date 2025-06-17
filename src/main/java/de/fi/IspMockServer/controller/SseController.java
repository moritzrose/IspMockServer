package de.fi.IspMockServer.controller;

import de.fi.IspMockServer.emitter.RingingEmitter;
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

    public static final Map<String, RingingEmitter> emitter = new HashMap<>();
    //private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @GetMapping("/{sessionId}")
    public SseEmitter ringingEmitter(@PathVariable String sessionId) {
        final RingingEmitter ringingEmitter = new RingingEmitter(0L);

        ringingEmitter.onCompletion(() -> {
            ringingEmitter.complete();
            emitter.remove(sessionId);
        });

        ringingEmitter.onTimeout(() -> {
            ringingEmitter.complete();
            emitter.remove(sessionId);
        });

        ringingEmitter.onError((e) -> {
            ringingEmitter.completeWithError(e);
            emitter.remove(sessionId);
        });

        emitter.put(sessionId, ringingEmitter);
        return ringingEmitter;
    }
}
