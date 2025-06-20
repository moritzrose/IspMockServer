package de.fi.IspMockServer.entitys;

import de.fi.IspMockServer.entitys.huawei.Content;

import java.util.Stack;

public class UserSession {

    private String sessionId;
    private String username;
    private State state;
    private String guid;
    private String lastEvent;
    private String metadata;
    private Content callData;
    private Content callInfo;
    private Stack<String> responses = new Stack<>();

    public Stack<String> getResponses() {
        return responses;
    }

    public void setResponses(Stack<String> responses) {
        this.responses = responses;
    }

    public Content getCallInfo() {
        return callInfo;
    }

    public void setCallInfo(Content callInfo) {
        this.callInfo = callInfo;
    }

    public UserSession(String agentId) {
        this.sessionId = agentId;
        this.username = agentId;
        this.state = State.PENDING;
    }

    public Content getMetaData() {
        return callData;
    }

    public void setCallData(Content callData) {
        this.callData = callData;
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

    public String getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(String lastEvent) {
        this.lastEvent = lastEvent;
    }
}
