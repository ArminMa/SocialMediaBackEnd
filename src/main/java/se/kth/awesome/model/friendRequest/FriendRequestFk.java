package se.kth.awesome.model.friendRequest;

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
public class FriendRequestFk implements Serializable, Comparable<FriendRequestFk> {

	private UserEntity requestFrom;
	private UserEntity requestTo;


	public FriendRequestFk() {
	}

	public FriendRequestFk(UserEntity requestFrom, UserEntity requestTo) {
		this.requestFrom = requestFrom;
		this.requestTo = requestTo;
	}


	@ManyToOne(fetch = FetchType.EAGER,  targetEntity = UserEntity.class)
	@JoinColumn(name = "request_from_user_id")
	public UserEntity getRequestFrom() {
		return requestFrom;
	}
	public void setRequestFrom(UserEntity requestFrom) {
		this.requestFrom = requestFrom;
	}


	@ManyToOne(fetch = FetchType.EAGER,  targetEntity = UserEntity.class)
	@JoinColumn(name = "request_to_user_id")
	public UserEntity getRequestTo() {
		return requestTo;
	}
	public void setRequestTo(UserEntity requestTo) {
		this.requestTo = requestTo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FriendRequestFk that = (FriendRequestFk) o;

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
	public int compareTo(FriendRequestFk o) {
		int thisTime = this.hashCode();
		long anotherEntity = o.hashCode();
		return (thisTime<anotherEntity ? -1 : (thisTime==anotherEntity ? 0 : 1));
	}

	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}
}
