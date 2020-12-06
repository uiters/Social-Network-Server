package uit.core.event;

public enum Level {
    NO_CARE(0,"don't care"),
    START_INTERESTED(1, "Start have a interested for your post"),
    INTERESTED(2, "really interested"),
    VERY_INTERESTED(3, "very interested");

    private final long code;
    private final String level;

    Level(long code, String level) {
        this.code = code;
        this.level = level;
    }

    public long getCode() {
        return code;
    }

    public String getLevel() {
        return level;
    }
}
