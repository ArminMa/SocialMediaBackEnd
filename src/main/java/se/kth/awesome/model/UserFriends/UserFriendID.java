package se.kth.awesome.model.UserFriends;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
@Embeddable
public class UserFriendID implements Serializable, Comparable<UserFriendID>{

	public UserEntity accepter;
	public UserEntity requester;

	public UserFriendID() {

	}

	public UserFriendID(UserEntity user, UserEntity friend) {
		this.accepter = user;
		this.requester = friend;
	}

	@ManyToOne(fetch = FetchType.EAGER,  targetEntity = UserEntity.class)
	@JoinColumn(name = "requester_user_id")
	public UserEntity getRequester() {
		return requester;
	}
	public void setRequester(UserEntity requester) {
		this.requester = requester;
	}

	@ManyToOne(fetch = FetchType.EAGER,  targetEntity = UserEntity.class)
	@JoinColumn(name = "accepter_user_id")
	public UserEntity getAccepter() {
		return accepter;
	}
	public void setAccepter(UserEntity accepter) {
		this.accepter = accepter;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserFriendID that = (UserFriendID) o;

		if (accepter != null ? !accepter.equals(that.accepter) : that.accepter != null) return false;
		return requester != null ? requester.equals(that.requester) : that.requester == null;

	}

	@Override
	public int hashCode() {
		int result = accepter != null ? accepter.hashCode() : 0;
		result = 31 * result + (requester != null ? requester.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(UserFriendID o) {
		int thisTime = this.hashCode();
		long anotherEntity = o.hashCode();
		return (thisTime < anotherEntity ? -1 : (thisTime == anotherEntity ? 0 : 1));
	}


	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}
}
