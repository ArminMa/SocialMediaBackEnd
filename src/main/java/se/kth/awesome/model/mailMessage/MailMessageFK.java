package se.kth.awesome.model.mailMessage;


import com.fasterxml.jackson.annotation.JsonInclude;
import se.kth.awesome.model.User.UserEntity;
import se.kth.awesome.util.gsonX.GsonX;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @ManyToOne(fetch = FetchType.EAGER/*,  targetEntity = UserEntity.class*/ )
    @JoinColumn(name = "mail_message_receiver_id")
    public UserEntity getReceiver() {
        return receiver;
    }
    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    @ManyToOne(fetch = FetchType.EAGER/*, targetEntity = UserEntity.class*/)
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

    @Override
    public String toString() {
        return GsonX.gson.toJson(this);
    }

}
