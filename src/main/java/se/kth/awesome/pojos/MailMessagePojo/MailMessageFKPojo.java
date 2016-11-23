package se.kth.awesome.pojos.MailMessagePojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import se.kth.awesome.pojos.UserPojo;
import se.kth.awesome.util.gson.GsonX;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailMessageFKPojo implements java.io.Serializable, Comparable<MailMessageFKPojo>{
    private UserPojo sender;
    private UserPojo receiver;

    public MailMessageFKPojo() {
    }

    public MailMessageFKPojo(UserPojo sender, UserPojo receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public UserPojo getReceiver() {
        return receiver;
    }
    public void setReceiver(UserPojo receiver) {
        this.receiver = receiver;
    }

    public UserPojo getSender() {
        return sender;
    }
    public void setSender(UserPojo sender) {
        this.sender = sender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MailMessageFKPojo that = (MailMessageFKPojo) o;

        if (sender != null ? !sender.equals(that.sender) : that.sender != null) return false;
        return receiver != null ? receiver.equals(that.receiver) : that.receiver == null;

    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(MailMessageFKPojo o) {
        int thisTime = this.hashCode();
        long anotherEntity = o.hashCode();
        return (thisTime<anotherEntity ? -1 : (thisTime==anotherEntity ? 0 : 1));
    }

    @Override
    public String toString() {
        return GsonX.gson.toJson(this);
    }
}
