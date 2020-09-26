package uit.core.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PostResponse {
    private Boolean hasNext;
    private String nextLink;
    private List<PostItem> items;

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

    public List<PostItem> getItems() {
        return items;
    }

    public void setItems(List<PostItem> items) {
        this.items = items;
    }
}
