package uit.core.entity.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long action;
    private long point;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAction() {
        return action;
    }

    public void setAction(long action) {
        this.action = action;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }
}
