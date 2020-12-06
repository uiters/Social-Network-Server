package uit.core.event;

public enum Action {
    LIKE(1, "LIKE"),
    COMMENT(2, "COMMENT"),
    CHAT(3, "CHAT"),
    MEETING(4,"MEETING");

    private final long code;
    private final String action;

    Action(long code, String action) {
        this.code = code;
        this.action = action;
    }

    public long getCode() {
        return code;
    }

    public String getAction() {
        return action;
    }
}
