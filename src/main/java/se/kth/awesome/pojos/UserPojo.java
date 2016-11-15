package se.kth.awesome.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import se.kth.awesome.model.FriendRequest;
import se.kth.awesome.util.GsonX;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@JsonInclude(JsonInclude.Include.NON_NULL )

public class UserPojo  implements Serializable,Comparable<UserPojo>{
    private Long id;
    private String userName;
    private String email;
    private String password;
    private byte[] picture; // Todo implement this Armin. low prio
    @JsonInclude(JsonInclude.Include.NON_EMPTY )
    private Collection<FriendRequestPojo> friendRequests = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY )
    private Collection<MailMessagePojo> mailMessages = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY )
    private Collection<UserPojo> friends = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY )
    private Collection<PostPojo> log = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY )
    private Collection<ChatMessagePojo> chatMessages = new ArrayList<>();


    public UserPojo() {}

    public UserPojo(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Collection<FriendRequestPojo> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(Collection<FriendRequestPojo> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public Collection<MailMessagePojo> getMailMessages() {
        return mailMessages;
    }

    public void setMailMessages(Collection<MailMessagePojo> mailMessages) {
        this.mailMessages = mailMessages;
    }

    public Collection<UserPojo> getFriends() {
        return friends;
    }

    public void setFriends(Collection<UserPojo> friends) {
        this.friends = friends;
    }

    public Collection<PostPojo> getLog() {
        return log;
    }

    public void setLog(Collection<PostPojo> log) {
        this.log = log;
    }

    public Collection<ChatMessagePojo> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(Collection<ChatMessagePojo> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPojo userPojo = (UserPojo) o;

        if (id != null ? !id.equals(userPojo.id) : userPojo.id != null) return false;
        if (userName != null ? !userName.equals(userPojo.userName) : userPojo.userName != null) return false;
        if (email != null ? !email.equals(userPojo.email) : userPojo.email != null) return false;
        if (password != null ? !password.equals(userPojo.password) : userPojo.password != null) return false;
        return Arrays.equals(picture, userPojo.picture);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(picture);
        return result;
    }

    @Override
    public int compareTo(UserPojo o) {
        int thisObject= this.hashCode();
        long anotherObject = o.hashCode();
        return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
    }

    @Override
    public String toString() {
        if(this.friendRequests != null && this.friendRequests.isEmpty()){
            friendRequests = null;
        }
        if(this.mailMessages != null && this.mailMessages.isEmpty()){
            this.mailMessages = null;
        }
        if(this.friends != null && this.friends.isEmpty()){
            this.friends = null;
        }
        if(this.log != null && this.log.isEmpty()){
            this.log = null;
        }
        if(this.chatMessages != null && this.chatMessages.isEmpty()){
            this.chatMessages = null;
        }

        String thisJsonString = GsonX.gson.toJson(this);

        if(this.friendRequests == null){
            friendRequests = new ArrayList<>();
        }
        if(this.mailMessages == null){
            this.mailMessages = new ArrayList<>();
        }
        if(this.friends == null){
            this.friends = new ArrayList<>();
        }
        if(this.log == null){
            this.log = new ArrayList<>();
        }
        if(this.chatMessages == null){
            this.chatMessages = new ArrayList<>();
        }

        return thisJsonString;
    }
}
