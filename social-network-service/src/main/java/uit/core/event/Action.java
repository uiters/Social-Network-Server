package uit.core.event;

public enum Action {
    LIKE(1, 1),
    COMMENT(2, 2),
    SAVE_POST(3,  4),
    READ_DETAIL(4,3),
    READ_15S(5,1);

    private final long code;
    private final long point;

    Action(long code, long point) {
        this.code = code;
        this.point = point;
    }

    public long getCode() {
        return code;
    }

    public long getPoint() {
        return point;
    }
}
