package de.fi.IspMockServer.entitys;

public class Event {
    public static final String RINGING = "ringing";
    public static final String CONNECTED = "connected";
    public static final String RELEASED = "released";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String READY = "bereit";

    private String type;
    private int bitMask;

    public String getType() {
        return type;
    }

    public int getBitMask() {
        return bitMask;
    }
}
