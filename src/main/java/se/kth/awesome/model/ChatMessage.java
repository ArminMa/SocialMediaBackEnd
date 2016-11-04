//package se.kth.awesome.model;
//
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.format.annotation.DateTimeFormat;
//
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//public class ChatMessage {
//
//    private Long id;
//    private String chatContent;
//    private Date sentDate;
//
//    public ChatMessage() {
//    }
//
//    public ChatMessage(UserEntity sender, UserEntity receiver, String postContent, Date postedDate) {
//        this.sender = sender;
//        this.receiver = receiver;
//        this.chatContent = postContent;
//        this.sentDate = postedDate;
//    }
//
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
//    private UserEntity sender;
//    @Column(name = "sender")
//    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    public UserEntity getSender() {
//        return sender;
//    }
//    public void setSender(UserEntity sender) {
//        this.sender = sender;
//    }
//
//    private UserEntity receiver;
//    @Column(name = "receiver")
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    public UserEntity getReceiver() {
//        return receiver;
//    }
//    public void setReceiver(UserEntity receiver) {
//        this.receiver = receiver;
//    }
//
//
//}
