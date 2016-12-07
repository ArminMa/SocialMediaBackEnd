package se.kth.awesome.model.friendRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class FriendRequestPojo implements Serializable, Comparable<FriendRequestPojo>{


	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public boolean isNew() {
		return (this.id == null);
	}

	public FriendRequestPojo() {
	}

	/**
	 * @param requestFrom sender of the request
	 * @param requestTo receiver of the request
	 *
	 * new FriendRequest( requestFromUser, requestToUser)
	 */
	public FriendRequestPojo(UserPojo requestFrom, UserPojo requestTo) {
		this.pk = new FriendRequestFkPojo(requestFrom, requestTo);
	}

	/**
	 *
	 * @param requestFrom sender of the request
	 * @param requestTo receiver of the request
	 * @param requestedDate request sent this date
	 * new FriendRequest( requestFromUser, requestToUser, new Date())
	 */
	public FriendRequestPojo(UserPojo requestFrom, UserPojo requestTo, Date requestedDate) {
		this.pk = new FriendRequestFkPojo(requestFrom, requestTo);
		this.requestedDate = requestedDate;
	}

	public FriendRequestFkPojo pk;
	public FriendRequestFkPojo getPk() {
		return pk;
	}
	public void setPk(FriendRequestFkPojo receivedMailID) {
		this.pk = receivedMailID;
	}



	public UserPojo getRequestTo() {
		return getPk().getRequestTo();
	}
	public void setRequestTo(UserPojo requestTo) {
		getPk().setRequestTo(requestTo);
	}


	public UserPojo getRequestFrom() {
		return pk.getRequestFrom();
	}
	public void setRequestFrom(UserPojo receivingUser) {
		getPk().setRequestFrom(receivingUser);
	}

	private Date requestedDate;
	public Date getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	private Date acceptedDate;
	public Date getAcceptedDate() {
		return acceptedDate;
	}
	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FriendRequestPojo that = (FriendRequestPojo) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (pk != null ? !pk.equals(that.pk) : that.pk != null) return false;
		if (requestedDate != null ? !requestedDate.equals(that.requestedDate) : that.requestedDate != null)
			return false;
		return acceptedDate != null ? acceptedDate.equals(that.acceptedDate) : that.acceptedDate == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (pk != null ? pk.hashCode() : 0);
		result = 31 * result + (requestedDate != null ? requestedDate.hashCode() : 0);
		result = 31 * result + (acceptedDate != null ? acceptedDate.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(FriendRequestPojo o) {
		int thisTime = this.hashCode();
		long anotherEntity = o.hashCode();
		return (thisTime<anotherEntity ? -1 : (thisTime==anotherEntity ? 0 : 1));
	}

	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}
}
