package de.fi.IspMockServer.service;

import de.fi.IspMockServer.entitys.Button;
import de.fi.IspMockServer.entitys.Function;
import de.fi.IspMockServer.entitys.UserSession;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SoftphoneService {

    private static final int INITIAL_STATE = 160; // "Nicht Bereit" ist immer Bit 6, Error-Tester 8
    private final Map<String, Object> softphones = new HashMap<>();

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
        for (Function function : Function.values()) {
            buttonPanel.put(function.getBit(), new Button(function.name(), function.getName(), function.getBitMask()));
        }
        final String agentId = userSession.getSessionId();
        softphones.put(agentId, buttonPanel);

        // je Button - ein Bit
        final int maxBit = Function.values().length;
        final int[] binaryForm = toBinary(INITIAL_STATE, maxBit);

        // aktiviere initialen Buttonstate
        for (int i = 0; i < binaryForm.length; i++) {
            boolean isEnabled = binaryForm[i] == 1;
            // i+1, da beim 1. Bit angefangen wird, nicht beim 0.
            buttonPanel.get(i + 1).setEnabled(isEnabled);
        }
        return buttonPanel;
    }

    public Map<Integer, Button> handleAction(UserSession userSession, Function function) {

        final Map<Integer, Button> buttonPanel = (Map<Integer, Button>) softphones.get(userSession.getSessionId());


        try {
            final HttpStatusCode status = function.execute();
            if (status.is2xxSuccessful()) {
                userSession.setState(function.getState());
                //wenn erfolgreich - passe Softphone-Display an

                // je Button - ein Bit
                final int maxBit = Function.values().length;
                final int[] binaryForm = toBinary(function.getBitMask(), maxBit);

                for (int i = 0; i < binaryForm.length; i++) {
                    boolean isEnabled = binaryForm[i] == 1;
                    // i+1, da beim 1. Bit angefangen wird, nicht beim 0.
                    buttonPanel.get(i + 1).setEnabled(isEnabled);
                }
            } else {
                System.out.println(String.format("Action: %s konnte nicht verarbeitet werden - Http-Request nicht erfolgreich", function.getName()));
            }
        } catch (Exception e) {
            System.out.println(String.format("Action: %s konnte nicht verarbeitet werden", function.getName()));
        }
        return buttonPanel;
    }

    public Map<Integer, Button> getButtonPanel(UserSession userSession) {
        final String id = userSession.getSessionId();
        return (Map<Integer, Button>) softphones.get(id);
    }

    public Map<Integer, Button> handleEvent(String sessionId, int bitMask) {
        Map<Integer, Button> buttonPanel = (Map<Integer, Button>) softphones.get(sessionId);

        final int maxBit = Function.values().length;
        final int[] binaryForm = toBinary(bitMask, maxBit);

        for (int i = 0; i < binaryForm.length; i++) {
            boolean isEnabled = binaryForm[i] == 1;
            // i+1, da beim 1. Bit angefangen wird, nicht beim 0.
            buttonPanel.get(i + 1).setEnabled(isEnabled);
        }

        return buttonPanel;
    }

    public void removeButtonPanel(String sessionId) {
        softphones.remove(sessionId);
    }
}
