package se.kth.awesome.model.role.rolePojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolesPojo implements Serializable, Comparable<RolesPojo> {



	private Role role;


	public RolesPojo() {

	}

	public RolesPojo(Role theRoles) {
		this.role = theRoles;
		this.id = (long) (theRoles.ordinal()+1);

	}


	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RolesPojo rolesPojo = (RolesPojo) o;

		if (role != rolesPojo.role) return false;
		return id != null ? id.equals(rolesPojo.id) : rolesPojo.id == null;
	}

	@Override
	public int hashCode() {
		int result = role != null ? role.hashCode() : 0;
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(RolesPojo o) {
		int thisObject= this.hashCode();
		long anotherObject = o.hashCode();
		return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
	}

	@Override
	public String toString() {
		return GsonX.gson.toJson(this);
	}

}
