package se.kth.awesome.model.mailMessage;


import se.kth.awesome.model.UserEntity;
import se.kth.awesome.model.chatMessage.ChatFK;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class MailMessageFK implements java.io.Serializable, Comparable<MailMessageFK>{
    private UserEntity sender;
    private UserEntity receiver;

    public MailMessageFK() {
    }

    public MailMessageFK(UserEntity sender, UserEntity receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @ManyToOne(fetch = FetchType.LAZY,  targetEntity = UserEntity.class)
    @JoinColumn(name = "mail_message_receiver_id")
    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    @JoinColumn(name = "mail_message_sender_id")
    public UserEntity getSender() {
        return sender;
    }
    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    @Override
    public int compareTo(MailMessageFK o) {
        int thisTime = this.hashCode();
        long anotherEntity = o.hashCode();
        return (thisTime<anotherEntity ? -1 : (thisTime==anotherEntity ? 0 : 1));
    }

}
