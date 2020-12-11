package uit.core.event;

public enum Level {
    NO_CARE(0,0),
    START_INTERESTED(1, 5),
    INTERESTED(2, 8),
    VERY_INTERESTED(3, 11);

    private final long code;
    private final long defaultPoint;

    Level(long code, long defaultPoint) {
        this.code = code;
        this.defaultPoint = defaultPoint;
    }

    public long getCode() {
        return code;
    }

    public long getDefaultPoint() {
        return defaultPoint;
    }
}
