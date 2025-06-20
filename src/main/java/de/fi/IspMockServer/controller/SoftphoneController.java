package de.fi.IspMockServer.controller;


import de.fi.IspMockServer.entitys.UserSession;
import de.fi.IspMockServer.entitys.huawei.Content;
import de.fi.IspMockServer.entitys.huawei.EventDto;
import de.fi.IspMockServer.entitys.huawei.EventResponseDto;
import de.fi.IspMockServer.service.SessionService;
import de.fi.IspMockServer.service.SoftphoneService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Stack;

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

    @GetMapping
    public String home(Model model, HttpServletRequest httpServletRequest) {
        final HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            final String agentId = (String) session.getAttribute("agentId");
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession != null) {
                model.addAttribute("agentId", agentId);
            }
            return "home";
        }
        return "login";
    }

    @PostMapping("/command/{agentId}")
    @ResponseBody
    public int command(@PathVariable String agentId, @RequestParam String command) {
        try {
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession == null) {
                return 500;
            }
            softphoneService.executeCommand(userSession, command);
        } catch (Exception e) {
            return 500;
        }
        return 500;
    }

    @PostMapping("/event/{agentId}")
    @ResponseBody
    public EventResponseDto event(@PathVariable String agentId, @RequestBody EventDto eventDto) {
        try {
            System.out.println("INCOMING: " + eventDto.toJson());
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

    @GetMapping("/panel/{agentId}")
    public String panel(Model model, @PathVariable String agentId) {
        try {
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession == null) {
                return "error";
            }
            model.addAttribute("buttons", softphoneService.getButtonPanel(userSession));
            model.addAttribute("agentId", agentId);
            return "panel";
        } catch (Exception e) {
            return "error";
        }
    }


    @GetMapping("/status/{agentId}")
    public String status(Model model, @PathVariable String agentId) {
        try {
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession == null) {
                return "error";
            }
            model.addAttribute("username", userSession.getUsername());
            model.addAttribute("state", userSession.getState());
            return "status";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/metadata/{agentId}")
    public String metadata(Model model, @PathVariable String agentId) {
        try {
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession == null) {
                return "error";
            }
            Content metaData = userSession.getMetaData();
            if (metaData == null) {
                model.addAttribute("metadata", "No Meta-Data");
                return "metadata";
            }
            model.addAttribute("metadata", metaData.toJson());
            return "metadata";

        } catch (Exception e) {
            return "error";
        }
    }


    @GetMapping("/responses/{agentId}")
    public String responses(Model model, @PathVariable String agentId) {
        try {
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession == null) {
                return "error";
            }

            Stack<String> responses = userSession.getResponses();
            if (responses.empty()) {
                responses = new Stack<>();
                responses.push("Response-List empty");
            }
            model.addAttribute("responses", responses);
            return "httpResponse";

        } catch (Exception e) {
            return "error";
        }
    }

    @DeleteMapping("/responses/{agentId}")
    public String clearResponses(Model model, @PathVariable String agentId) {
        try {
            final UserSession userSession = sessionService.findUserSession(agentId);
            if (userSession == null) {
                return "error";
            }
            userSession.getResponses().clear();
            userSession.getResponses().push("Response-List empty");

            model.addAttribute("responses", userSession.getResponses());
            return "httpResponse";

        } catch (Exception e) {
            return "error";
        }
    }
}
