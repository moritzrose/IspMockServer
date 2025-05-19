package de.fi.IspMockServer.entitys;

public class UserSession {

    private String sessionId;
    private String username;
    private State state;

    /**
     * Initial State: NOT_READY
     */
    public UserSession(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        this.state = State.NOT_READY;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
