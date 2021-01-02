//package uit.core.entity;
//
//import javax.persistence.*;
//
//@Entity
//public class GroupInvitation {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(name="`from`")
//    private Long from;
//
//    @Column(name="`to`")
//    private Long to;
//
//    private Long groupId;
//    private Long status;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getFrom() {
//        return from;
//    }
//
//    public void setFrom(Long from) {
//        this.from = from;
//    }
//
//    public Long getTo() {
//        return to;
//    }
//
//    public void setTo(Long to) {
//        this.to = to;
//    }
//
//    public Long getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(Long groupId) {
//        this.groupId = groupId;
//    }
//
//    public Long getStatus() {
//        return status;
//    }
//
//    public void setStatus(Long status) {
//        this.status = status;
//    }
//}
