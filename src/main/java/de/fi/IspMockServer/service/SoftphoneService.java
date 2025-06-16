package de.fi.IspMockServer.service;

import de.fi.IspMockServer.controller.SseController;
import de.fi.IspMockServer.emitter.RingingEmitter;
import de.fi.IspMockServer.entitys.Button;
import de.fi.IspMockServer.entitys.EventDto;
import de.fi.IspMockServer.entitys.State;
import de.fi.IspMockServer.entitys.UserSession;
import de.fi.IspMockServer.entitys.XEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SoftphoneService {
    private final Map<String, Object> softphones = new HashMap<>();

    private final HttpService httpService;

    public SoftphoneService(HttpService httpService) {
        this.httpService = httpService;
    }

    public static int[] toBinary(long bitMask, int maxBit) {
        final int[] result = new int[maxBit];
        long rest = bitMask;
        for (int i = maxBit; i > 0; i--) {
            int bitValue = (int) Math.pow(2, i - 1);
            int bit = (rest / bitValue != 0) ? 1 : 0;
            result[i - 1] = bit;
            if (bit == 1) {
                rest -= bitValue;
            }
        }
        if (rest != 0) {
            throw new RuntimeException(String.format("Wert: %d kann nicht mit %d Bits dargestellt werden", bitMask, maxBit));
        }
        return result;
    }

    public Map<Integer, Button> setupButtonpanel(UserSession userSession) {
        // erzeuge neues Buttonpanel für neuen User
        final Map<Integer, Button> buttonPanel = new HashMap<>();
        for (Button.Function function : Button.Function.values()) {
            buttonPanel.put(function.getBit(), new Button(function.name(), function.getLabel()));
        }
        final String agentId = userSession.getSessionId();
        softphones.put(agentId, buttonPanel);
        return buttonPanel;
    }

    public Optional<String> handleAction(UserSession userSession, String action) {

        // um Warten auf Event zu simulieren
        userSession.setState(State.PENDING);
        updateButtonPanel((Map<Integer, Button>) softphones.get(userSession.getSessionId()), State.PENDING);

        try {
            switch (Button.Function.valueOf(action)) {
                case ANSWER_CALL:
                    return answerCall(userSession);
                case HOLD_CALL:
                    return holdCall(userSession);
                case UNHOLD_CALL:
                    return unholdCall(userSession);
                case INITIATE_CALL:
                    return initiateCall(userSession);
                case RELEASE_CALL:
                    return releaseCall(userSession);
                case SET_AGENT_NOT_READY:
                    return setAgentNotReady(userSession);
                case SET_AGENT_READY:
                    return setAgentReady(userSession);
            }
        } catch (Exception e) {
            System.out.printf("Action: %s konnte nicht verarbeitet werden: %s%n", action, e);
        }
        return Optional.empty();
    }

    private Optional<String> unholdCall(UserSession userSession) {
        return httpService.unholdCall();
    }

    private Optional<String> setAgentReady(UserSession userSession) {
        return httpService.setAgentReady(userSession);
    }

    private Optional<String> setAgentNotReady(UserSession userSession) {
        return httpService.setAgentNotReady();
    }

    private Optional<String> releaseCall(UserSession userSession) {
        return httpService.releaseCall();
    }

    private Optional<String> initiateCall(UserSession userSession) {
        return httpService.initiateCall(userSession);
    }

    private Optional<String> holdCall(UserSession userSession) {
        return httpService.holdCall();
    }

    private Optional<String> answerCall(UserSession userSession) {
        return httpService.answerCall();
    }

    private void updateButtonPanel(Map<Integer, Button> buttonPanel, State state) {
        final int[] binaryForm = toBinary(state.getBitMask(), buttonPanel.size());
        for (int i = 0; i < binaryForm.length; i++) {
            boolean isEnabled = binaryForm[i] == 1;
            // i+1, da beim 1. Bit angefangen wird, nicht beim 0.
            buttonPanel.get(i + 1).setEnabled(isEnabled);
        }
    }

    public String ringing(XEvent XEvent, UserSession userSession) {
        final String sessionId = userSession.getSessionId();
        if (XEvent.getType().equals("Ringing")) {

            // hole SSE Ringing Emitter zur UserSession // TODO Rename/Refactor RingingEmitter
            final RingingEmitter ringingEmitter = SseController.emitter.get(sessionId);
            try {
                ringing(userSession);
                ringingEmitter.emit();
                return String.format("200: Ringing Event an %s gesendet.", sessionId);
            } catch (Exception e) {
                ringingEmitter.completeWithError(e);
                return String.format("500: Konnte kein Ringing Event an %s senden.", sessionId);
            }
        }
        return "Event-Type nicht bekannt.";
    }

    private void ringing(UserSession userSession) {
        userSession.setState(State.RINGING);
        final String sessionId = userSession.getSessionId();
        final Map<Integer, Button> buttonPanel = (Map<Integer, Button>) softphones.get(sessionId);
        updateButtonPanel(buttonPanel, State.RINGING);
    }

    public Map<Integer, Button> getButtonPanel(UserSession userSession) {
        final String id = userSession.getSessionId();
        return (Map<Integer, Button>) softphones.get(id);
    }

    public void removeButtonPanel(String sessionId) {
        softphones.remove(sessionId);
    }

    public void handleEvent(UserSession userSession, EventDto eventDto) {
        final Map<Integer, Button> buttonPanel = (Map<Integer, Button>) softphones.get(userSession.getSessionId());
        final String eventType = eventDto.getEvent().getEventType();

        switch (eventType) {
            case "AgentState_LoggedIn":
            case "AgentState_NotReady":
                userSession.setState(State.NOT_READY);
                updateButtonPanel(buttonPanel, State.NOT_READY);
                break;
            case "AgentState_LoggedOut":
                // Todo?
                break;
            case "AgentState_Ready":
            case "AgentState_CallReleased":
                userSession.setState(State.READY);
                updateButtonPanel(buttonPanel, State.READY);
                break;
            case "AgentState_UnholdCall":
                userSession.setState(State.IN_CALL);
                updateButtonPanel(buttonPanel, State.IN_CALL);
                break;
            case "AgentState_InitiateCall":
                userSession.setState(State.CALLING);
                updateButtonPanel(buttonPanel, State.CALLING);
                break;
            case "AgentState_HoldCall":
                userSession.setState(State.HOLDING);
                updateButtonPanel(buttonPanel, State.HOLDING);
                break;
        }
    }
}
