package uit.core.event;

public enum Action {
    LIKE(1, 1, "Like"),
    COMMENT(2, 2, "Comment"),
    SAVE_POST(3,  4, "Save a post"),
    READ_DETAIL(4,3, "Read detail of a post"),
    READ_15S(5,1, "Read a post in 15s");

    private final long code;
    private final long point;
    private final String name;

    Action(long code, long point,String name) {
        this.code = code;
        this.point = point;
        this.name = name;
    }

    public long getCode() {
        return code;
    }

    public long getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    public static Action getAction(long code) {
        for (Action action : Action.values()) {
            if (action.getCode() == code) return action;
        }
        return LIKE;
    }
}
