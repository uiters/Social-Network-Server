package uit.core.event;

import org.springframework.context.ApplicationEvent;
import uit.core.entity.event.UserAction;

public class CareEvent extends ApplicationEvent {
    private UserAction userAction;

    public CareEvent(Object source, UserAction userAction) {
        super(source);
        this.userAction = userAction;
    }

    public UserAction getUserAction() {
        return userAction;
    }

    public void setUserAction(UserAction userAction) {
        this.userAction = userAction;
    }

    @Override
    public String toString() {
        return "CareEvent{" +
                "userAction=" + userAction +
                '}';
    }
}
