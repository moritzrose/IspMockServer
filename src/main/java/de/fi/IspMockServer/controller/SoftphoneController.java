package de.fi.IspMockServer.controller;


import de.fi.IspMockServer.entitys.EventDto;
import de.fi.IspMockServer.entitys.EventResponseDto;
import de.fi.IspMockServer.entitys.UserSession;
import de.fi.IspMockServer.service.SessionService;
import de.fi.IspMockServer.service.SoftphoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/softphone")
public class SoftphoneController {

    private final SoftphoneService softphoneService;
    private final SessionService sessionService;

    @Autowired
    public SoftphoneController(SessionService sessionService, SoftphoneService softphoneService) {
        this.softphoneService = softphoneService;
        this.sessionService = sessionService;
    }

    @GetMapping // intern
    public String home(Model model, HttpServletRequest httpServletRequest) {
        final HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            final String sessionId = (String) session.getAttribute("sessionId");
            final UserSession userSession = sessionService.findUserSession(sessionId);
            if (userSession != null) {
                model.addAttribute("buttons", softphoneService.getButtonPanel(userSession));
                model.addAttribute("userSession", userSession);
                //if (userSession.getLastEvent() != null) {
                //    model.addAttribute("lastEvent", userSession.getLastEvent());
                //}
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

    @PostMapping("/event/{agentId}") //extern {agentId}
    @ResponseBody
    public EventResponseDto event(@PathVariable String agentId, @RequestBody EventDto eventDto) {
        try {
            System.out.println(eventDto.toJson());
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession == null) {
                return new EventResponseDto("failure", "1");
            }
            userSession.setLastEvent(eventDto.toJson());
            softphoneService.handleEvent(userSession, eventDto);
            return new EventResponseDto("success", "0");

        } catch (Exception e) {
            return new EventResponseDto("failure", "1");
        }
    }
}
