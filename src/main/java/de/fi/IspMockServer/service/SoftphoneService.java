package de.fi.IspMockServer.service;

import de.fi.IspMockServer.controller.SseController;
import de.fi.IspMockServer.emitter.RingingEmitter;
import de.fi.IspMockServer.entitys.Button;
import de.fi.IspMockServer.entitys.Event;
import de.fi.IspMockServer.entitys.State;
import de.fi.IspMockServer.entitys.UserSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SoftphoneService {
    private final Map<String, Object> softphones = new HashMap<>();

    private HttpService httpService = HttpService.getInstance();

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

        updateButtonPanel(buttonPanel, State.NOT_READY);
        return buttonPanel;
    }

    public Map<Integer, Button> handleAction(UserSession userSession, String action) {

        final Map<Integer, Button> buttonPanel = (Map<Integer, Button>) softphones.get(userSession.getSessionId());
        try {
            switch (Button.Function.valueOf(action)) {
                case ANSWER_CALL:
                    answerCall(userSession, buttonPanel);
                    break;
                case HOLD_CALL:
                    holdCall(userSession, buttonPanel);
                    break;
                case UNHOLD_CALL:
                    unholdCall(userSession, buttonPanel);
                    break;
                case INITIATE_CALL:
                    initiateCall(userSession, buttonPanel);
                    break;
                case RELEASE_CALL:
                    releaseCall(userSession, buttonPanel);
                    break;
                case SET_AGENT_NOT_READY:
                    setAgentNotReady(userSession, buttonPanel);
                    break;
                case SET_AGENT_READY:
                    setAgentReady(userSession, buttonPanel);
                    break;
            }
        } catch (Exception e) {
            System.out.println(String.format("Action: %s konnte nicht verarbeitet werden", action));
        }
        return buttonPanel;
    }

    private void unholdCall(UserSession userSession, Map<Integer, Button> buttonPanel) {
        try {
            httpService.unholdCall();
        } catch (Exception e) {
        }
        userSession.setState(State.IN_CALL);
        updateButtonPanel(buttonPanel, State.IN_CALL);
    }

    private void setAgentReady(UserSession userSession, Map<Integer, Button> buttonPanel) {
        try {
            httpService.setAgentReady();
        } catch (Exception e) {
        }
        userSession.setState(State.READY);
        updateButtonPanel(buttonPanel, State.READY);
    }

    private void setAgentNotReady(UserSession userSession, Map<Integer, Button> buttonPanel) {
        try {
            httpService.setAgentNotReady();
        } catch (Exception e) {
        }
        userSession.setState(State.NOT_READY);
        updateButtonPanel(buttonPanel, State.NOT_READY);
    }

    private void releaseCall(UserSession userSession, Map<Integer, Button> buttonPanel) {
        try {
            httpService.releaseCall();
        } catch (Exception e) {
        }
        userSession.setState(State.READY);
        updateButtonPanel(buttonPanel, State.READY);
    }

    private void initiateCall(UserSession userSession, Map<Integer, Button> buttonPanel) {
        try {
            httpService.initiateCall();
        } catch (Exception e) {
        }
        userSession.setState(State.CALLING);
        updateButtonPanel(buttonPanel, State.CALLING);
    }

    private void holdCall(UserSession userSession, Map<Integer, Button> buttonPanel) {
        try {
            httpService.holdCall();
        } catch (Exception e) {
        }
        userSession.setState(State.HOLDING);
        updateButtonPanel(buttonPanel, State.HOLDING);
    }

    private void answerCall(UserSession userSession, Map<Integer, Button> buttonPanel) {
        try {
            httpService.answerCall();
        } catch (Exception e) {
        }
        userSession.setState(State.IN_CALL);
        updateButtonPanel(buttonPanel, State.IN_CALL);
    }

    private void updateButtonPanel(Map<Integer, Button> buttonPanel, State state) {
        final int[] binaryForm = toBinary(state.getBitMask(), buttonPanel.size());
        for (int i = 0; i < binaryForm.length; i++) {
            boolean isEnabled = binaryForm[i] == 1;
            // i+1, da beim 1. Bit angefangen wird, nicht beim 0.
            buttonPanel.get(i + 1).setEnabled(isEnabled);
        }
    }

    public String handleEvent(Event event, UserSession userSession) {
        final String sessionId = userSession.getSessionId();
        if (event.getType().equals("Ringing")) {

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
        return String.format("Event-Type nicht bekannt.");
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
}
