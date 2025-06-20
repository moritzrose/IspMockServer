package de.fi.IspMockServer.service;

import de.fi.IspMockServer.entitys.UserSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {
    private Map<String, UserSession> userSessions = new HashMap<>();

    public UserSession findUserSession(String sessionId) {
        return userSessions.get(sessionId);
    }

    public UserSession initiateSession(String agentId) {
        final UserSession userSession = new UserSession(agentId);
        userSessions.put(agentId, userSession);
        return userSession;
    }

    public void removeSession(String sessionId) {
        userSessions.remove(sessionId);
    }
}
