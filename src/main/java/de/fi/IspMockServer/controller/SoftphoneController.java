package de.fi.IspMockServer.controller;


import de.fi.IspMockServer.entitys.Event;
import de.fi.IspMockServer.entitys.Function;
import de.fi.IspMockServer.entitys.State;
import de.fi.IspMockServer.entitys.UserSession;
import de.fi.IspMockServer.service.SessionService;
import de.fi.IspMockServer.service.SoftphoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/softphone")
public class SoftphoneController {

    private SoftphoneService softphoneService;
    private SessionService sessionService;

    @Autowired
    public SoftphoneController(SoftphoneService softphoneService, SessionService sessionService) {
        this.softphoneService = softphoneService;
        this.sessionService = sessionService;
    }

    @GetMapping() // intern
    public String home(Model model, HttpServletRequest httpServletRequest) {
        final HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            final String sessionId = (String) session.getAttribute("sessionId");
            final UserSession userSession = sessionService.findUserSession(sessionId);
            if (userSession != null) {
                model.addAttribute("buttons", softphoneService.getButtonPanel(userSession));
                model.addAttribute("userSession", userSession);
            }
        }
        return "softphoneMock";
    }

    @PostMapping("/action") //intern
    public String action(Model model, HttpServletRequest httpServletRequest, @RequestParam String action) {
        final HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            final String sessionId = (String) session.getAttribute("sessionId");
            final UserSession userSession = sessionService.findUserSession(sessionId);
            if (userSession != null) {
                final Function function = Function.valueOf(action);
                model.addAttribute("buttons", softphoneService.handleAction(userSession, function));
                model.addAttribute("userSession", userSession);
            }
        }
        return "softphoneMock";
    }

    @PostMapping("/ringing") //extern
    @ResponseBody
    public String event(@RequestBody Event event) {
        final String sessionId = event.getSessionId();
        final UserSession userSession = sessionService.findUserSession(sessionId);
        if (userSession != null) {
            if (userSession.getState().equals(State.READY)) {
                // hole Emitter zur UserSession
                final SseEmitter ringingEmitter = SseController.emitter.get(sessionId);
                try {
                    userSession.setState(State.RINGING);
                    softphoneService.handleEvent(sessionId, State.RINGING.getBitMask());

                    //.data leer aber notwendig, da sonst hx-trigger: sse:ringing nicht funktioniert
                    ringingEmitter.send(SseEmitter.event().name("ringing").data(""));
                    System.out.println(String.format("200: Ringing Event an %s gesendet.", sessionId));
                } catch (Exception e) {
                    ringingEmitter.completeWithError(e);
                    System.out.println(String.format("500: Konnte kein Ringing Event an %s senden.", sessionId));
                }
                return String.format("200", event.getType());
            } else {
                return String.format("User: %s nicht bereit.", sessionId);
            }
        }
        return String.format("SessionId: %s existiert nicht.", sessionId);
    }

    @GetMapping("/test")
    public String test() {
        return "answerButton";
    }
}
