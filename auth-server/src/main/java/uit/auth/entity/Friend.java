package uit.auth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long firstUser;
    private Long secondUser;
    private Long status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(Long firstUser) {
        this.firstUser = firstUser;
    }

    public Long getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(Long secondUser) {
        this.secondUser = secondUser;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
