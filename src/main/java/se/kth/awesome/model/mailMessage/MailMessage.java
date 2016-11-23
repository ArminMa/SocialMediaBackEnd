package se.kth.awesome.model.mailMessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import se.kth.awesome.model.UserEntity;
import se.kth.awesome.util.gson.GsonX;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class MailMessage implements Serializable,Comparable<MailMessage>{

    private Long id;
    private String messageContent;
    private String topic;
    private Date sentDate;
    //TODO implement boolen/replacement for read messages
//    private boolean read;

    public MailMessage() {
            pk = new MailMessageFK();
    }

    public MailMessage(String messageContent, String topic, UserEntity sender, UserEntity receiver) {
        this.messageContent = messageContent;
        this.topic = topic;
        this.sentDate = new Date();
        this.pk = new MailMessageFK(sender, receiver);
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", insertable = false, updatable = false, unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "message_content")
    public String getMessageContent() {
        return messageContent;
    }
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Column(name = "topic")
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    private MailMessageFK pk;
    @Embedded
    public MailMessageFK getPk() {
        return pk;
    }
    public void setPk(MailMessageFK mailMessageId) {
        this.pk = mailMessageId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreatedDate
    @Column(name = "sent_date")
    public Date getSentDate() {
        return sentDate;
    }
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
    @Transient
    public UserEntity getReceiver() {
        return getPk().getReceiver();
    }
    public void setReceiver(UserEntity receivingUser) {
        getPk().setReceiver(receivingUser);
    }

    @Transient
    public UserEntity getSender() {
        return getPk().getSender();
    }
    public void setSender(UserEntity sender) {
        getPk().setSender(sender);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MailMessage that = (MailMessage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return sentDate != null ? sentDate.equals(that.sentDate) : that.sentDate == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sentDate != null ? sentDate.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(MailMessage o) {
        int thisObject= this.hashCode();
        long anotherObject = o.hashCode();
        return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
    }


    @Override
    public String toString() {
        return GsonX.gson.toJson(this);
    }
}
