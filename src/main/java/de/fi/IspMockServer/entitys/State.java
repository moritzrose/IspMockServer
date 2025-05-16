package de.fi.IspMockServer.entitys;

public enum State {
    READY(19),
    NOT_READY(32),
    CALLING(8),
    IN_CALL(12),
    HOLDING(72),
    RINGING(2);

    private final int bitMask;

    State(int bitMask) {
        this.bitMask = bitMask;
    }

    public int getBitMask() {
        return bitMask;
    }
}
