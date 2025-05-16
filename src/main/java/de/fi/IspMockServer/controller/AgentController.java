package de.fi.IspMockServer.controller;


import de.fi.IspMockServer.entitys.Function;
import de.fi.IspMockServer.entitys.State;
import de.fi.IspMockServer.entitys.UserSession;
import de.fi.IspMockServer.service.SessionService;
import de.fi.IspMockServer.service.SoftphoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/agent")
public class AgentController {

    private SoftphoneService softphoneService;
    private SessionService sessionService;

    @Autowired
    public AgentController(SoftphoneService softphoneService, SessionService sessionService) {
        this.softphoneService = softphoneService;
        this.sessionService = sessionService;
    }

    @GetMapping("/softphone") // intern
    public String home(Model model) {
        String sessionId = "dummyId"; //TODO ID muss irgendwoher kommen
        final UserSession userSession = sessionService.findUserSession(sessionId);
        if (userSession != null) {
            model.addAttribute("buttons", softphoneService.getButtonPanel(userSession));
            model.addAttribute("state", userSession.getState());
        }
        return "softphoneMock";
    }

    @PostMapping("/softphone/login") //intern
    public String login(Model model, @RequestBody String username) {
        final String sessionId = "dummyId"; //TODO ID muss irgendwoher kommen
        final UserSession userSession = sessionService.initiateSession(sessionId, username);
        model.addAttribute("buttons", softphoneService.setupButtonpanel(userSession));
        model.addAttribute("state", userSession.getState());
        return "softphoneMock";
    }

    @PostMapping("/softphone/action") //intern
    public String action(Model model, @RequestParam String action) {
        final String sessionId = "dummyId";
        final Function function = Function.valueOf(action);
        final UserSession userSession = sessionService.findUserSession(sessionId);
        model.addAttribute("buttons", softphoneService.handleAction(userSession, function));
        model.addAttribute("state", userSession.getState());
        return "softphoneMock";
    }

    @PostMapping("/softphone/event") //extern
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
