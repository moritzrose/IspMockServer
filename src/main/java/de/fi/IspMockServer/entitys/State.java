package de.fi.IspMockServer.entitys;

public enum State {
    READY(17), //0
    NOT_READY(32), //1
    CALLING(8), //2
    IN_CALL(12),//3
    HOLDING(72),//4
    RINGING(2),//5
    PENDING(0),//6
    ACW(32),//7
    AUFZEICHNEN(0);//8

    private final int bitMask;

    State(int bitMask) {
        this.bitMask = bitMask;
    }

    public int getBitMask() {
        return bitMask;
    }
}
