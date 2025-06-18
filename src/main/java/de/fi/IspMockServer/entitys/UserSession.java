package de.fi.IspMockServer.entitys;

public class UserSession {

    private String sessionId;
    private String username;
    private State state;
    private String skillId;
    private String guid;
    private String callId;
    private String lastEvent;
    private String metadata;

    public UserSession(String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        this.state = State.NOT_READY;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
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

    public String getSkillId() {
        return skillId;
    }

    public String getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(String lastEvent) {
        this.lastEvent = lastEvent;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}
