package uit.core.entity.event;

import javax.persistence.*;

@Entity
public class UserLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long userId;
    private long postId;
    private long levelId;

    @Column(columnDefinition = "integer default 0")
    private long point;

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getLevelId() {
        return levelId;
    }

    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }
}
