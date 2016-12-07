package se.kth.awesome.model.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SortNatural;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import se.kth.awesome.model.UserFriends.UserFriend;


import se.kth.awesome.model.friendRequest.FriendRequest;
import se.kth.awesome.model.role.UserRoleEntity;
import se.kth.awesome.util.gsonX.GsonX;


//import org.kth.HI1034.model.validators.ExtendedEmailValidator;
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "face_user", uniqueConstraints = {
		@UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "username")})
public class UserEntity implements Serializable,Comparable<UserEntity>{

	private Long id;
	private String username;
	private String email;
	private String password;
    private byte[] picture; // Todo implement this Armin. low prio


	public UserEntity() {
	}

	/**
	 * the param order
	 *
	 * @param email       1
	 * @param username    2
	 * @param password    3
	 *
	 */
	public UserEntity( String username,String email, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
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

	@Column(name = "username")
//	@NotEmpty
    @Length(max = 100)
    public String getUsername() {
        return username;
    }
    public void setUsername(String userName) {
        this.username = userName;
    }

	//	@Basic
//	@ExtendedEmailValidator
	@NotEmpty
	@Length(max = 240)
	@Column(unique = true, nullable = false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Column
	@NotEmpty
	@Length(max = 255)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

//	@Lob
//	public byte[] getPicture() {
//		return picture;
//	}
//
//	public void setPicture(byte[] picture) {
//		this.picture = picture;
//	}

	/*
	 * user Mapping starts Here
	 */
//---------------------------- authority ------------------------

//	CascadeType.DETACH means that if i disappear if the rows where i i'm in "in between this and other table"
// should be remove. we don't want that

//	CascadeType.REMOVE wants to remove the other side Entity if this is removed, that is not good hear.

	private Collection<FriendRequest> friendRequests = new TreeSet<>();
	@ManyToMany(/*cascade = {CascadeType.PERSIST, CascadeType.MERGE},*/)
	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
    public Collection<FriendRequest> getFriendRequests() {
        return friendRequests;
    }
    public void setFriendRequests(Collection<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }


	private Collection<UserRoleEntity> authorities = new TreeSet<>();
	@OneToMany( orphanRemoval = false, fetch = FetchType.EAGER)
	@BatchSize(size=3)
	@LazyCollection(LazyCollectionOption.FALSE)
	@SortNatural
	public Collection<UserRoleEntity> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Collection<UserRoleEntity> authorities) {
		this.authorities = authorities;
	}






	@Override
	public String toString() {
		if(this.friendRequests != null && this.friendRequests.isEmpty()){
			friendRequests = null;
		}
		if(this.authorities != null && this.authorities.isEmpty()){
			this.authorities = null;
		}

		String thisJsonString = GsonX.gson.toJson(this);

		if(this.friendRequests == null){
			this.friendRequests = new TreeSet<>();
		}

		if(this.authorities == null){
			this.authorities = new TreeSet<>();
		}

		return thisJsonString;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserEntity that = (UserEntity) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (username != null ? !username.equals(that.username) : that.username != null) return false;
		if (email != null ? !email.equals(that.email) : that.email != null) return false;
		return password != null ? password.equals(that.password) : that.password == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (username != null ? username.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(UserEntity o) {
		int thisObject= this.hashCode();
		long anotherObject = o.hashCode();
		return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
	}
}