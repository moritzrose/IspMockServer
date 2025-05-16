package de.fi.IspMockServer.entitys;

import de.fi.IspMockServer.service.HttpService;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;

public enum Function {

    INITIATE_CALL(State.CALLING, "Anrufen", 1, 8) { // 4

        @Override
        public HttpStatusCode execute() throws IOException {
            return httpService.initiateCall();
        }
    },
    ANSWER_CALL(State.IN_CALL, "Annehmen", 2, 12) { // 3,4

        @Override
        public HttpStatusCode execute() throws IOException {
            return httpService.answerCall();
        }
    },
    HOLD_CALL(State.HOLDING, "Halten", 3, 72) { // 4,7

        @Override
        public HttpStatusCode execute() throws IOException {
            return httpService.holdCall();
        }
    },
    UNHOLD_CALL(State.IN_CALL, "Fortführen", 7, 12) { // 3,4

        @Override
        public HttpStatusCode execute() throws IOException {
            return httpService.unholdCall();
        }
    },
    RELEASE_CALL(State.READY, "Auflegen", 4, 17) { // 1, 5

        @Override
        public HttpStatusCode execute() throws IOException {
            return httpService.releaseCall();
        }
    },
    SET_AGENT_NOT_READY(State.NOT_READY, "Nicht Bereit", 5, 32) { // 6

        @Override
        public HttpStatusCode execute() throws IOException {
            return httpService.setAgentNotReady();
        }
    },
    SET_AGENT_READY(State.READY, "Bereit", 6, 17) { // 1, 5

        @Override
        public HttpStatusCode execute() throws IOException {
            return httpService.setAgentReady();
        }
    },

    ERROR_TESTER(State.NOT_READY, "Error-Tester",8, 0){
        @Override
        public HttpStatusCode execute() throws IOException {
            return httpService.errorTester();
        }
    };

    private static final HttpService httpService = HttpService.getInstance();
    private final int bit;
    private final long bitMask;
    private final String name;
    private State state;

    Function(State state, String name, int bit, long bitMask) {
        this.name = name;
        this.bit = bit;
        this.bitMask = bitMask;
        this.state = state;
    }

    public abstract HttpStatusCode execute() throws IOException;

    public State getState() {
        return state;
    }

    public int getBit() {
        return bit;
    }

    public String getName() {
        return name;
    }

    public long getBitMask() {
        return bitMask;
    }
}
