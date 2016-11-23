package se.kth.awesome.model.chatMessage;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import se.kth.awesome.model.User.UserEntity;


@Entity
public class ChatMessage implements Serializable,Comparable<ChatMessage>{

    private Long id;
    private String chatContent;
    private Date sentDate;

    public ChatMessage() {
        pk = new ChatFK();
    }

    public ChatMessage(String chatContent, Date postedDate, UserEntity sender, UserEntity receiver) {
        this.pk = new ChatFK( receiver, sender);
        this.chatContent = chatContent;
        this.sentDate = postedDate;
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

    @Column(name = "chat_content")
    public String getChatContent() {
        return chatContent;
    }
    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @CreatedDate
    @Column(name = "sent_date",
            nullable = false,
            insertable = true,
            updatable = false)
    public Date getSentDate() {
        return sentDate;
    }
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public ChatFK pk;
    @Embedded
    public ChatFK getPk() {
        return pk;
    }
    public void setPk(ChatFK chatMessageID) {
        this.pk = chatMessageID;
    }

    @Transient
    public UserEntity getReceivingUser() {
        return getPk().getReceivingUser();
    }
    public void setReceivingUser(UserEntity receivingUser) {
        getPk().setReceivingUser(receivingUser);
    }

    @Transient
    public UserEntity getAuthor() {
        return getPk().getAuthor();
    }
    public void setAuthor(UserEntity author) {
        getPk().setAuthor(author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

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
    public int compareTo(ChatMessage o) {
        int thisObject= this.hashCode();
        long anotherObject = o.hashCode();
        return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
    }
}
