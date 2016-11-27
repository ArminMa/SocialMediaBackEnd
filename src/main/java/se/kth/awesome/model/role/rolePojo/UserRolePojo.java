package se.kth.awesome.model.role.rolePojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.util.gsonX.GsonX;


@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL )
public class UserRolePojo implements Serializable, Comparable<UserRolePojo>{
	private Long id;
	private Boolean isLocked = false;
	private RolesPojo authority;



	public UserRolePojo() {
	}

	public UserRolePojo(Role roleEnum) {
		this.authority = new RolesPojo(roleEnum);
		isLocked = false;
	}


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

	public RolesPojo getAuthority() {
		return authority;
	}
	public void setAuthority(RolesPojo rolesPojo) {
		this.authority= (rolesPojo);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserRolePojo that = (UserRolePojo) o;

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
	public int compareTo(UserRolePojo o) {
		int thisObject= this.hashCode();
		long anotherObject = o.hashCode();
		return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
	}

	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}

}
