package se.kth.awesome.model.post;


import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.User.UserEntity;
import se.kth.awesome.util.gson.GsonX;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@Embeddable
public class PostFK implements java.io.Serializable, Comparable<PostFK>{
    private UserEntity sender;
    private UserEntity receiver;

    public PostFK() {
    }

    public PostFK(UserEntity sender, UserEntity receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @ManyToOne(fetch = FetchType.EAGER,  targetEntity = UserEntity.class)
    @JoinColumn(name = "post_message_receiver_id")
    public UserEntity getReceiver() {
        if(receiver != null)
            receiver.setPassword(null);
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
    @JoinColumn(name = "post_message_sender_id")
    public UserEntity getSender() {
        if(sender != null)
            sender.setPassword(null);
        return sender;
    }
    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    @Override
    public int compareTo(PostFK o) {
        int thisTime = this.hashCode();
        long anotherEntity = o.hashCode();
        return (thisTime<anotherEntity ? -1 : (thisTime==anotherEntity ? 0 : 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostFK postFK = (PostFK) o;

        if (sender != null ? !sender.equals(postFK.sender) : postFK.sender != null) return false;
        return receiver != null ? receiver.equals(postFK.receiver) : postFK.receiver == null;
    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return GsonX.gson.toJson(this);
    }

}
