package uit.core.event;

public enum RecommendationType {
    PUSH_NOTI(Level.START_INTERESTED,1),
    CHAT(Level.INTERESTED, 2),
    MEETING( Level.VERY_INTERESTED, 3);


    private final Level level;
    private final long recommendationAction;

    RecommendationType(Level level, long recommendationAction) {
        this.level = level;
        this.recommendationAction = recommendationAction;
    }

    public Level getLevel() {
        return level;
    }

    public long getRecommendationAction() {
        return recommendationAction;
    }

    public static RecommendationType getRecommendationType(long level) {
        for (RecommendationType recommendationType : RecommendationType.values()) {
            if (level==recommendationType.getLevel().getCode()) {
                return recommendationType;
            }
        }
        return PUSH_NOTI;
    }
}
