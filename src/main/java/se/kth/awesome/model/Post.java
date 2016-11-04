//package se.kth.awesome.model;
//
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//public class Post {
//
//    private UserEntity sender;
//    private UserEntity receiver;
//    private String postContent;
//    private Date postedDate;
//
//
//    public Post() {
//    }
//
//    public Post(UserEntity sender, UserEntity receiver, String postContent, Date postedDate) {
//        this.sender = sender;
//        this.receiver = receiver;
//        this.postContent = postContent;
//        this.postedDate = postedDate;
//    }
//
//    private Long id;
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id", insertable = false, updatable = false, unique = true, nullable = false)
//    public Long getId() {
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    @Column(name = "sender")
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    public UserEntity getSender() {
//        return sender;
//    }
//    public void setSender(UserEntity sender) {
//        this.sender = sender;
//    }
//
//    @Column(name = "receiver")
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    public UserEntity getReceiver() {
//        return receiver;
//    }
//    public void setReceiver(UserEntity receiver) {
//        this.receiver = receiver;
//    }
//
//    @Column(name = "post_content")
//    public String getPostContent() {
//        return postContent;
//    }
//    public void setPostContent(String postContent) {
//        this.postContent = postContent;
//    }
//
//
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(pattern = "yyyy-MM-dd.hh:mm:ss.SSS")
//    @CreatedDate
//    @Column(name = "posted_date",
//            nullable = false,
//            insertable = true,
//            updatable = false)
//    public Date getPostedDate() {
//        return postedDate;
//    }
//    public void setPostedDate(Date postedDate) {
//        this.postedDate = postedDate;
//    }
//}
