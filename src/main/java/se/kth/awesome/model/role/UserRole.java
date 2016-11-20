package se.kth.awesome.model.role;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UserRole
 *
 * @author vladimir.stankovic
 *
 *         Aug 18, 2016
 */
@Entity
@Table(name = "user_role")
public class UserRole implements java.io.Serializable, Comparable<UserRole> {

	public UserRole() {
	}

	public UserRole(Role role) {
		this.id = (long) (role.ordinal()+1);
		this.role = role;
	}

	private Long id;
	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Role role;
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

//	public UserEntity userId;
//	@ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
//	@JoinColumn(name = "user_id")
//	public UserEntity getUserId() {
//		return userId;
//	}
//	public void setUserId(UserEntity userId) {
//		this.userId = userId;
//	}




	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserRole userRole = (UserRole) o;

		if (id != null ? !id.equals(userRole.id) : userRole.id != null) return false;
		return role == userRole.role;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (role != null ? role.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(UserRole o) {
		int thisObject= this.hashCode();
		long anotherObject = o.hashCode();
		return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
	}

}