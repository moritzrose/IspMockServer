package de.fi.IspMockServer.controller;


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
            final String sessionId = (String) session.getAttribute("userSessionId");
            final UserSession userSession = sessionService.findUserSession(sessionId);
            if (userSession != null) {
                model.addAttribute("buttons", softphoneService.getButtonPanel(userSession));
                model.addAttribute("usersession", userSession);
            }
        }
        return "softphoneMock";
    }

    @PostMapping("/action") //intern
    public String action(Model model, HttpServletRequest httpServletRequest, @RequestParam String action) {
        final HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            final String sessionId = (String) session.getAttribute("userSessionId");
            final UserSession userSession = sessionService.findUserSession(sessionId);
            if (userSession != null) {
                final Function function = Function.valueOf(action);
                model.addAttribute("buttons", softphoneService.handleAction(userSession, function));
                model.addAttribute("usersession", userSession);
            }
        }
        return "softphoneMock";
    }

    @PostMapping("/event") //extern
    public String event(@RequestBody String event) {
        final String sessionId = "dummyId";
        final UserSession userSession = sessionService.findUserSession(sessionId);
        if (userSession != null) {
            if (event.equals("Ringing")) {
                userSession.setState(State.RINGING);
                // TODO: irgendwie Browser zur SessionId aktualisieren
                return "softphoneMock";
            }
        }
        throw new RuntimeException(String.format("SessionId: %s existiert nicht.", sessionId));
        // sessionService routet Call je nach State der angemeldeten Agenten, Prio?
    }

}
