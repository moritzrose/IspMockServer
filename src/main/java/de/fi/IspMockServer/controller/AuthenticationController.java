package de.fi.IspMockServer.controller;

import de.fi.IspMockServer.entitys.UserSession;
import de.fi.IspMockServer.service.HttpService;
import de.fi.IspMockServer.service.SessionService;
import de.fi.IspMockServer.service.SoftphoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private SoftphoneService softphoneService;
    private SessionService sessionService;
    private HttpService httpService;

    @Autowired
    public AuthenticationController(HttpService httpService, SessionService sessionService, SoftphoneService softphoneService) {
        this.softphoneService = softphoneService;
        this.sessionService = sessionService;
        this.httpService = httpService;
    }

    @PostMapping("/login")
    public String login(Model model, HttpServletRequest httpServletRequest, @RequestParam String agentId) {
        try {
            final HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("agentId", agentId);
            final UserSession userSession = sessionService.initiateSession(agentId);
            softphoneService.setupButtonpanel(userSession);
            Optional<String> response = httpService.login(userSession);
            if (response.isEmpty()) {
                return "error";
            }
            model.addAttribute("agentId", agentId);
            return "home";
        } catch (Exception e) {
            LOGGER.error("login: ", e);
            return "error";
        }
    }

    @PostMapping("/logout/{agentId}")
    public String logout(HttpServletRequest httpServletRequest, @PathVariable String agentId) {
        try {
            final HttpSession session = httpServletRequest.getSession(false);
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession == null) {
                LOGGER.error("logout: userSession is null");
                return "error";
            }
            Optional<String> response = httpService.logOut(userSession);
            if (response.isEmpty()) {
                return "error";
            }
            sessionService.removeSession(agentId);
            softphoneService.removeButtonPanel(agentId);
            SseController.emitters.get(agentId).complete();
            SseController.emitters.remove(agentId);
            session.invalidate();
            return "login";
        } catch (Exception e) {
            LOGGER.error("logout: ", e);
            return "error";
        }
    }
}
