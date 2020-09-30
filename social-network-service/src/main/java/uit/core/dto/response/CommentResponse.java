package uit.core.dto.response;

import java.util.List;

public class CommentResponse {
    private Boolean hasNext;
    private String nextLink;
    private long postId;
    private List<CommentItem> items;

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

    public List<CommentItem> getItems() {
        return items;
    }

    public void setItems(List<CommentItem> items) {
        this.items = items;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }
}
