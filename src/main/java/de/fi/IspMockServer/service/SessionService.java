package de.fi.IspMockServer.service;

import de.fi.IspMockServer.entitys.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {
    private Map<String, UserSession> userSessions = new HashMap<>();

    public UserSession findUserSession(String sessionId) {
        return userSessions.get(sessionId);
    }

    public UserSession initiateSession(String sessionId, String username) {
        final UserSession userSession = new UserSession(sessionId, username);
        userSessions.put(sessionId, userSession);
        return userSession;
    }

    public void removeSession(String sessionId) {
        userSessions.remove(sessionId);
    }
}
