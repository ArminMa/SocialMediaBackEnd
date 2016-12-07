package se.kth.awesome.model.UserFriends;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import se.kth.awesome.model.post.PostPojo;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.util.gsonX.GsonX;


@XmlRootElement
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class UserFriendPojo implements Serializable, Comparable<UserFriendPojo> {


    public UserFriendPojo() {

    }

    public UserFriendPojo(UserPojo accepter, UserPojo requester, Date beginningOfFriendship) {
        this.pk = new UserFriendIdPojo(accepter, requester);
        this.friendshipBegan = beginningOfFriendship;
    }

    private Date friendshipBegan;
    public Date getFriendshipBegan() {
        return friendshipBegan;
    }
    public void setFriendshipBegan(Date friendshipBegan) {
        this.friendshipBegan = friendshipBegan;
    }

    private Date friendshipEndedn;
    public Date getFriendshipEndedn() {
        return friendshipEndedn;
    }
    public void setFriendshipEndedn(Date friendshipEndedn) {
        this.friendshipEndedn = friendshipEndedn;
    }


    private UserFriendIdPojo pk;
    public UserFriendIdPojo getPk() {
        return pk;
    }
    public void setPk(UserFriendIdPojo userFriendIdPojo) {
        this.pk = userFriendIdPojo;
    }

    public UserPojo getRequester() {
        return getPk().getRequester();
    }
    public void setRequester(UserPojo requester) {
        this.getPk().setRequester(requester);
    }

    public UserPojo getAccepter() {
        return getPk().getAccepter();
    }
    public void setAccepter(UserPojo accepter) {
        this.getPk().setAccepter(accepter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFriendPojo that = (UserFriendPojo) o;

        if (friendshipBegan != null ? !friendshipBegan.equals(that.friendshipBegan): that.friendshipBegan != null)
            return false;
        if (friendshipEndedn != null ? !friendshipEndedn.equals(that.friendshipEndedn) : that.friendshipEndedn != null)
            return false;
        return pk != null ? pk.equals(that.pk) : that.pk == null;
    }

    @Override
    public int hashCode() {
        int result = friendshipBegan != null ? friendshipBegan.hashCode() : 0;
        result = 31 * result + (friendshipEndedn != null ? friendshipEndedn.hashCode() : 0);
        result = 31 * result + (pk != null ? pk.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(UserFriendPojo o) {
        int thisTime = this.hashCode();
        long anotherEntity = o.hashCode();
        return (thisTime < anotherEntity ? -1 : (thisTime == anotherEntity ? 0 : 1));
    }

    @Override
    public String toString() {
        return GsonX.gson.toJson(this);
    }
}
