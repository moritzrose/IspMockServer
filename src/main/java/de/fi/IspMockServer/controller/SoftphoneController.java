package de.fi.IspMockServer.controller;


import de.fi.IspMockServer.entitys.EventDto;
import de.fi.IspMockServer.entitys.State;
import de.fi.IspMockServer.entitys.UserSession;
import de.fi.IspMockServer.entitys.XEvent;
import de.fi.IspMockServer.service.SessionService;
import de.fi.IspMockServer.service.SoftphoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/softphone")
public class SoftphoneController {

    private SoftphoneService softphoneService;
    private SessionService sessionService;

    @Autowired
    public SoftphoneController(SessionService sessionService, SoftphoneService softphoneService) {
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
                Optional<String> response = softphoneService.handleAction(userSession, action);
                model.addAttribute("buttons", softphoneService.getButtonPanel(userSession));
                model.addAttribute("userSession", userSession);
                response.ifPresent(s -> model.addAttribute("httpResponse", s));
            }
        }
        return "softphoneMock";
    }

    @PostMapping("/ringing") //extern
    @ResponseBody
    public String ringing(@RequestBody XEvent XEvent) {
        final String sessionId = XEvent.getSessionId();
        final UserSession userSession = sessionService.findUserSession(sessionId);
        if (userSession != null) {
            if (userSession.getState().equals(State.READY)) {
                return softphoneService.ringing(XEvent, userSession);
            } else {
                return String.format("User: %s nicht bereit.", sessionId);
            }
        }
        return String.format("SessionId: %s existiert nicht.", sessionId);
    }

    @PostMapping("/event/{agentId}") //extern {agentId}
    @ResponseBody
    public String event(@PathVariable String agentId, @RequestBody EventDto eventDto) {

        try {
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession == null) {
                return String.format("AgentId: %s existiert nicht", agentId);
            }
            softphoneService.handleEvent(userSession, eventDto);
            return "200";

        } catch (Exception e) {
            return "500";
        }
    }
}
