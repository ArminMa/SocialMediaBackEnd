package se.kth.awesome.model;//package se.kth.awesome.model;
//
//import org.hibernate.annotations.LazyCollection;
//import org.hibernate.annotations.LazyCollectionOption;
//import org.hibernate.annotations.SortNatural;
//
//import javax.persistence.*;
//import java.util.Date;
//@Entity
//public class MailMessage {
//
//    private UserEntity sender;
//    private UserEntity receiver;
//    private String messageContent;
//    private String topic;
//    private Date sentDate;
//    private boolean read;
//
//    public MailMessage() {
//    }
//
//    //måste fixa så att sender och receiver är id för dena class // databas class
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
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.EAGER/*, mappedBy = "UserEntity.friendRequests"*/)
////	@BatchSize(size=100)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public UserEntity getSender() {
//        return sender;
//    }
//    public void setSender(UserEntity sender) {
//        this.sender = sender;
//    }
//
//    @Column(name = "receiver")
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.EAGER/*, mappedBy = "UserEntity.friendRequests"*/)
////	@BatchSize(size=100)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public UserEntity getReceiver() {
//        return receiver;
//    }
//    public void setReceiver(UserEntity receiver) {
//        this.receiver = receiver;
//    }
//
//    @Column(name = "message_content")
//    public String getMessageContent() {
//        return messageContent;
//    }
//    public void setMessageContent(String messageContent) {
//        this.messageContent = messageContent;
//    }
//
//    @Column(name = "topic")
//    public String getTopic() {
//        return topic;
//    }
//    public void setTopic(String topic) {
//        this.topic = topic;
//    }
//
//    @Column(name = "sent_date")
//    public Date getSentDate() {
//        return sentDate;
//    }
//    public void setSentDate(Date sentDate) {
//        this.sentDate = sentDate;
//    }
//
//    @Column(name = "read")
//    public boolean isRead() {
//        return read;
//    }
//    public void setRead(boolean read) {
//        this.read = read;
//    }
//
////    @Column(name = "read")
////    public boolean getRead() {
////        return read;
////    }
//
//
//
//}
