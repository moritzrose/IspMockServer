package de.fi.IspMockServer.entitys;

public class Button {

    private final String label;
    private final String command;
    private boolean enabled;

    public enum Command {

        INITIATE_CALL("Anrufen", 1), // 4
        ANSWER_CALL("Annehmen", 2), // 3,4
        HOLD_CALL("Halten", 3), // 4,7
        RELEASE_CALL("Auflegen", 4),  // 1, 5
        SET_AGENT_NOT_READY("Nicht Bereit", 5),// 6
        SET_AGENT_READY("Bereit", 6),// 1, 5
        UNHOLD_CALL("Fortführen", 7); // 3,4

        private final int bit;
        private final String label;

        Command(String label, int bit) {
            this.label = label;
            this.bit = bit;
        }

        public int getBit() {
            return bit;
        }

        public String getLabel() {
            return label;
        }
    }

    public Button(String command, String label) {
        this.command = command;
        this.label = label;
        this.enabled = false;
    }

    public String getCommand() {
        return command;
    }

    public String getLabel() {
        return label;
    }

    public boolean isEnabled() {
        return true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
