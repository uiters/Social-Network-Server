package uit.core.entity.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long level;
    private long activePoint;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public long getActivePoint() {
        return activePoint;
    }

    public void setActivePoint(long activePoint) {
        this.activePoint = activePoint;
    }
}
