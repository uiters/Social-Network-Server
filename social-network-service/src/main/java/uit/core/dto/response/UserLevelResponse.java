package uit.core.dto.response;

import java.util.List;

public class UserLevelResponse {
    private Boolean hasNext;
    private String nextLink;
    private List<UserLevelItem> items;

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

    public List<UserLevelItem> getItems() {
        return items;
    }

    public void setItems(List<UserLevelItem> items) {
        this.items = items;
    }
}
