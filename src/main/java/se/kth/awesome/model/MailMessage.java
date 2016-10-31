//package se.kth.awsome.model;
//
//import org.hibernate.annotations.LazyCollection;
//import org.hibernate.annotations.LazyCollectionOption;
//import org.hibernate.annotations.SortNatural;
//import org.hibernate.validator.constraints.Length;
//import org.hibernate.validator.constraints.NotEmpty;
//
//import javax.persistence.*;
//import java.util.Date;
//@Entity
//public class MailMessage {
//
//    private ApplicationUser sender;
//    private ApplicationUser receiver;
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
//    @Column(name = "sender")
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.EAGER/*, mappedBy = "ApplicationUser.friendRequests"*/)
////	@BatchSize(size=100)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public ApplicationUser getSender() {
//        return sender;
//    }
//    public void setSender(ApplicationUser sender) {
//        this.sender = sender;
//    }
//
//    @Column(name = "receiver")
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.EAGER/*, mappedBy = "ApplicationUser.friendRequests"*/)
////	@BatchSize(size=100)
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @SortNatural
//    public ApplicationUser getReceiver() {
//        return receiver;
//    }
//    public void setReceiver(ApplicationUser receiver) {
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
