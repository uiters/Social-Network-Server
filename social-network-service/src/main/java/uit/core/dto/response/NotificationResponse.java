package uit.core.dto.response;

import uit.core.entity.Notification;

import java.util.List;

public class NotificationResponse {
    private Boolean hasNext;
    private String nextLink;
    private List<Notification> items;

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getNextLink() {
        return nextLink;
    }

    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    public List<Notification> getItems() {
        return items;
    }

    public void setItems(List<Notification> items) {
        this.items = items;
    }
}
