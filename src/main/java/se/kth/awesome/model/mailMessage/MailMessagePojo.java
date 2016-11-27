package se.kth.awesome.model.mailMessage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.util.gsonX.GsonX;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailMessagePojo implements Serializable,Comparable<MailMessagePojo>{
    private Long id;
    private String messageContent;
    private String topic;

//    private boolean read;

    public MailMessagePojo() {
        this.pk = new MailMessageFKPojo();
    }

    public MailMessagePojo(String messageContent, String topic, UserPojo sender, UserPojo receiver) {
        this.messageContent = messageContent;
        this.topic = topic;
        this.sentDate = new Date(/*System.currentTimeMillis()*/);
        this.pk = new MailMessageFKPojo(sender, receiver);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    private MailMessageFKPojo pk;
    public   MailMessageFKPojo getPk() {
        return pk;
    }
    public void setPk(  MailMessageFKPojo mailMessageId) {
        this.pk = mailMessageId;
    }

    public UserPojo getReceiver() {
        return getPk().getReceiver();
    }
    public void setReceiver(UserPojo receivingUser) {
        getPk().setReceiver(receivingUser);
    }

    public UserPojo getSender() {
        return getPk().getSender();
    }
    public void setSender(UserPojo sender) {
        getPk().setSender(sender);
    }

    public String getMessageContent() {
        return messageContent;
    }
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }


	private Date sentDate;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    public Date getSentDate() {
        return sentDate;
    }
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }


//    public boolean isRead() {
//        return read;
//    }
//
//    public boolean getRead() {
//        return read;
//    }
//    public void setRead(boolean read) {
//        this.read = read;
//    }

    @Override
    public String toString() {


        return GsonX.gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MailMessagePojo that = (MailMessagePojo) o;

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
    public int compareTo(MailMessagePojo o) {
        int thisObject= this.hashCode();
        long anotherObject = o.hashCode();
        return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
    }
}
