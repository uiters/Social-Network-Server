package uit.core.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PostItem {
    private long id;

    private String title;
    private String description;
    private long typeBusiness;
    private long typeProperty;
    private long area;
    private String district;
    private String address;
    private long roomNumber;
    private long priceFrom;
    private long priceTo;

    private long userId;
    private String username;
    private long groupId;

    private long totalLike;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date expiredAt;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private List<String> images;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTypeBusiness() {
        return typeBusiness;
    }

    public void setTypeBusiness(long typeBusiness) {
        this.typeBusiness = typeBusiness;
    }

    public long getTypeProperty() {
        return typeProperty;
    }

    public void setTypeProperty(long typeProperty) {
        this.typeProperty = typeProperty;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public long getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(long priceFrom) {
        this.priceFrom = priceFrom;
    }

    public long getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(long priceTo) {
        this.priceTo = priceTo;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public long getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(long totalLike) {
        this.totalLike = totalLike;
    }
}
