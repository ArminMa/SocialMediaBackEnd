package se.kth.awesome.model.friendRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
@Entity
@Table(name = "friend_request", indexes =
		{
				@Index(name = "from_user_id_idx", columnList = "from_user_id"),
				@Index(name = "to_user_id_idx", columnList = "to_user_id")
		}
)
@AssociationOverrides({
		@AssociationOverride(name = "pk.requestTo",
				joinColumns = @JoinColumn(name = "to_user_id")),
		@AssociationOverride(name = "pk.requestFrom",
				joinColumns = @JoinColumn(name = "from_user_id")) })
public class FriendRequest implements Serializable, Comparable<FriendRequest> {

	private Long id;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", insertable=false, updatable=false, unique=true, nullable=false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	public boolean isNew() {
		return (this.id == null);
	}

	public FriendRequest() {
	}

	/**
	 * @param requestFrom sender of the request
	 * @param requestTo receiver of the request
	 *
	 * new FriendRequest( requestFromUser, requestToUser)
	 */
	public FriendRequest(UserEntity requestFrom, UserEntity requestTo) {
		this.pk = new FriendRequestFk(requestFrom, requestTo);
	}

	/**
	 *
	 * @param requestFrom sender of the request
	 * @param requestTo receiver of the request
	 * @param requestedDate request sent this date
	 * new FriendRequest( requestFromUser, requestToUser, new Date())
	 */
	public FriendRequest(UserEntity requestFrom, UserEntity requestTo, Date requestedDate) {
		this.pk = new FriendRequestFk(requestFrom, requestTo);
		this.requestedDate = requestedDate;
	}

	public FriendRequestFk pk;
//	@Fetch(FetchMode.JOIN)
	@Embedded
	public FriendRequestFk getPk() {
		return pk;
	}
	public void setPk(FriendRequestFk receivedMailID) {
		this.pk = receivedMailID;
	}


	@Transient
	public UserEntity getRequestTo() {
		return getPk().getRequestTo();
	}
	public void setRequestTo(UserEntity requestTo) {
		getPk().setRequestTo(requestTo);
	}

	@Transient
	public UserEntity getRequestFrom() {
		return pk.getRequestFrom();
	}
	public void setRequestFrom(UserEntity receivingUser) {
		getPk().setRequestFrom(receivingUser);
	}

	private Date requestedDate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy.MM.dd.hh.mm.ss.SSS")
	@NotNull
	@CreatedDate
	@Column(name = "requested_date",
			nullable = true,
			insertable = true,
			updatable = true)

	public Date getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	private Date acceptedDate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy.MM.dd.hh.mm.ss.SSS")
//	@NotNull
//	@Past
	@CreatedDate
	@Column(name = "accepted_date",
			nullable = true,
			insertable = true,
			updatable = true)
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

		FriendRequest that = (FriendRequest) o;

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
	public int compareTo(FriendRequest o) {
		int thisTime = this.hashCode();
		long anotherEntity = o.hashCode();
		return (thisTime<anotherEntity ? -1 : (thisTime==anotherEntity ? 0 : 1));
	}

	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}
}
