//package se.kth.awsome.model;
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
//    private ApplicationUser sender;
//    private ApplicationUser receiver;
//    private String postContent;
//    private Date postedDate;
//
//
//    public Post() {
//    }
//
//    public Post(ApplicationUser sender, ApplicationUser receiver, String postContent, Date postedDate) {
//        this.sender = sender;
//        this.receiver = receiver;
//        this.postContent = postContent;
//        this.postedDate = postedDate;
//    }
//
//    @Column(name = "sender")
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    public ApplicationUser getSender() {
//        return sender;
//    }
//    public void setSender(ApplicationUser sender) {
//        this.sender = sender;
//    }
//
//    @Column(name = "receiver")
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    public ApplicationUser getReceiver() {
//        return receiver;
//    }
//    public void setReceiver(ApplicationUser receiver) {
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
