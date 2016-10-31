//package se.kth.awsome.model;
//
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//public class ChatMessage {
//
//    private String chatContent;
//    private Date sentDate;
//
//    public ChatMessage() {
//    }
//
//    public ChatMessage(ApplicationUser sender, ApplicationUser receiver, String postContent, Date postedDate) {
//        this.sender = sender;
//        this.receiver = receiver;
//        this.chatContent = postContent;
//        this.sentDate = postedDate;
//    }
//
//
//    @Column(name = "post_content")
//    public String getChatContent() {
//        return chatContent;
//    }
//    public void setChatContent(String chatContent) {
//        this.chatContent = chatContent;
//    }
//
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @DateTimeFormat(pattern = "yyyy-MM-dd.hh:mm:ss.SSS")
//    @CreatedDate
//    @Column(name = "posted_date",
//            nullable = false,
//            insertable = true,
//            updatable = false)
//    public Date getSentDate() {
//        return sentDate;
//    }
//    public void setSentDate(Date sentDate) {
//        this.sentDate = sentDate;
//    }
//
//
//
//
//    private ApplicationUser sender;
//    @Column(name = "sender")
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    public ApplicationUser getSender() {
//        return sender;
//    }
//    public void setSender(ApplicationUser sender) {
//        this.sender = sender;
//    }
//
//    private ApplicationUser receiver;
//    @Column(name = "receiver")
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    public ApplicationUser getReceiver() {
//        return receiver;
//    }
//    public void setReceiver(ApplicationUser receiver) {
//        this.receiver = receiver;
//    }
//
//
//}
