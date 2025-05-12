package de.fi.IspMockServer;

public class Event {
    public static final String RINGING = "ringing";
    public static final String CONNECTED = "connected";
    public static final String RELEASED = "released";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";

    private String type;
    private int bitMask;

    public String getType() {
        return type;
    }

    public int getBitMask() {
        return bitMask;
    }
}
