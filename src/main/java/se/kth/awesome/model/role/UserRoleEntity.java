package se.kth.awesome.model.role;


import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "user_roles_entity")
public class UserRoleEntity implements java.io.Serializable, Comparable<UserRoleEntity> {
	private Long id;
	private Boolean isLocked = false;
	private RolesEntity authority;


	public UserRoleEntity() {
	}

	public UserRoleEntity( Role roleEnum) {
		this.authority = new RolesEntity(roleEnum);
		isLocked = false;
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


	public Boolean getLocked() {
		return isLocked;
	}

	public void setLocked(Boolean locked) {
		isLocked = locked;
	}

	@ManyToOne( fetch = FetchType.EAGER)
	public RolesEntity getAuthority() {
		return authority;
	}
	public void setAuthority(RolesEntity rolesEntity) {
		this.authority= (rolesEntity);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserRoleEntity that = (UserRoleEntity) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		return isLocked != null ? isLocked.equals(that.isLocked) : that.isLocked == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (isLocked != null ? isLocked.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(UserRoleEntity o) {
		int thisObject= this.hashCode();
		long anotherObject = o.hashCode();
		return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
	}

	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}


}