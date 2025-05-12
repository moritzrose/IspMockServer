package de.fi.IspMockServer.entitys;

public class Button {
    private String label;
    private String event;

    public Button(String label, String event) {
        this.label = label;
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public String getLabel() {
        return label;
    }
}
