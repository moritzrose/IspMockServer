package de.fi.IspMockServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sse")
public class SseController {

    public static final Map<String, SseEmitter> emitter = new HashMap<>();
    //private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @GetMapping("/ringing/{sessionId}")
    public SseEmitter streamSseMvc(@PathVariable String sessionId) {
        final SseEmitter ringingEmitter = new SseEmitter(0L);

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

        /*Runnable ping = () -> {

            try {
                ringingEmitter.send(SseEmitter.event()
                        .name("ringing").data(String.format("<div>%s</div>", LocalTime.now())));
                //ringingEmitter.send(String.format("<div>%s</div>", LocalTime.now()));
                System.out.println(String.format("Ping an %s gesendet.", sessionId));
            } catch (Exception e) {
                System.out.println(String.format("Ping konnte nicht an %s gesendet werden.", sessionId));
            }
        };
        scheduler.scheduleAtFixedRate(ping, 10, 5, TimeUnit.SECONDS);*/
        emitter.put(sessionId, ringingEmitter);
        return ringingEmitter;
    }
}
