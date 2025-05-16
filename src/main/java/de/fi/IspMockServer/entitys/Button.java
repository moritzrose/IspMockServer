package de.fi.IspMockServer.entitys;

public class Button {

    private final String name;
    private final String label;
    private final long bitMask;
    private boolean enabled;

    public Button(String name, String label, long bitMask) {
        this.name = name;
        this.label = label;
        this.bitMask = bitMask;
        this.enabled = false;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public long getBitMask() {
        return bitMask;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
