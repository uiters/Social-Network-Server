package uit.core.dto.response;

import uit.core.entity.Report;

import java.util.List;

public class ReportResponse {
    private Boolean hasNext;
    private String nextLink;
    private List<Report> items;

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

    public List<Report> getItems() {
        return items;
    }

    public void setItems(List<Report> items) {
        this.items = items;
    }
}
