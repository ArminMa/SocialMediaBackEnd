package se.kth.awesome.model.UserFriends;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class UserFriendIdPojo implements Serializable, Comparable<UserFriendIdPojo>{

	public UserPojo accepter;
	public UserPojo requester;

	public UserFriendIdPojo() {

	}

	public UserFriendIdPojo(UserPojo user, UserPojo friend) {
		this.accepter = user;
		this.requester = friend;
	}


	public UserPojo getRequester() {
		return requester;
	}
	public void setRequester(UserPojo requester) {
		this.requester = requester;
	}


	public UserPojo getAccepter() {
		return accepter;
	}
	public void setAccepter(UserPojo accepter) {
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
	public int compareTo(UserFriendIdPojo o) {
		int thisTime = this.hashCode();
		long anotherEntity = o.hashCode();
		return (thisTime < anotherEntity ? -1 : (thisTime == anotherEntity ? 0 : 1));
	}


	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}

}
