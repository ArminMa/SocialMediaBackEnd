package se.kth.awesome.model.friendRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class FriendRequestFkPojo implements Serializable, Comparable<FriendRequestFkPojo>{

	private UserPojo requestFrom;
	private UserPojo requestTo;


	public FriendRequestFkPojo() {
	}

	public FriendRequestFkPojo(UserPojo requestFrom, UserPojo requestTo) {
		this.requestFrom = requestFrom;
		this.requestTo = requestTo;
	}

	public UserPojo getRequestFrom() {
		return requestFrom;
	}
	public void setRequestFrom(UserPojo requestFrom) {
		this.requestFrom = requestFrom;
	}


	public UserPojo getRequestTo() {
		return requestTo;
	}
	public void setRequestTo(UserPojo requestTo) {
		this.requestTo = requestTo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FriendRequestFkPojo that = (FriendRequestFkPojo) o;

		if (requestFrom != null ? !requestFrom.equals(that.requestFrom) : that.requestFrom != null) return false;
		return requestTo != null ? requestTo.equals(that.requestTo) : that.requestTo == null;

	}

	@Override
	public int hashCode() {
		int result = requestFrom != null ? requestFrom.hashCode() : 0;
		result = 31 * result + (requestTo != null ? requestTo.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(FriendRequestFkPojo o) {
		int thisTime = this.hashCode();
		long anotherEntity = o.hashCode();
		return (thisTime<anotherEntity ? -1 : (thisTime==anotherEntity ? 0 : 1));
	}

	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}
}
