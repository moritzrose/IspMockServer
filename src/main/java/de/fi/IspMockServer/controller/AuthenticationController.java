package de.fi.IspMockServer.controller;

import de.fi.IspMockServer.entitys.UserSession;
import de.fi.IspMockServer.service.HttpService;
import de.fi.IspMockServer.service.SessionService;
import de.fi.IspMockServer.service.SoftphoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    private SoftphoneService softphoneService;
    private SessionService sessionService;
    private HttpService httpService = HttpService.getInstance();

    @Autowired
    public AuthenticationController(SessionService sessionService, SoftphoneService softphoneService) {
        this.softphoneService = softphoneService;
        this.sessionService = sessionService;
    }

    @PostMapping("/login") //intern
    public String login(Model model, HttpServletRequest httpServletRequest, @RequestParam String username) {
        final HttpSession session = httpServletRequest.getSession(true);
        final String sessionId = username;
        session.setAttribute("sessionId", sessionId);
        final UserSession userSession = sessionService.initiateSession(sessionId, username);
        softphoneService.setupButtonpanel(userSession);
        final Optional<String> response = httpService.login(userSession);
        response.ifPresent(s -> model.addAttribute("httpResponse", s));
        model.addAttribute("buttons", softphoneService.setupButtonpanel(userSession));
        model.addAttribute("userSession", userSession);
        return "softphoneMock";
    }

    @PostMapping("/logout") //intern
    public String logout(Model model, HttpServletRequest httpServletRequest) {
        final HttpSession session = httpServletRequest.getSession(false);
        final String sessionId = (String) session.getAttribute("sessionId");
        sessionService.removeSession(sessionId);
        softphoneService.removeButtonPanel(sessionId);
        SseController.emitter.get(sessionId).complete();
        SseController.emitter.remove(sessionId);
        session.invalidate();
        final Optional<String> response = httpService.logOut(sessionId);
        response.ifPresent(s -> model.addAttribute("httpResponse", s));
        return "softphoneMock";
    }
}
