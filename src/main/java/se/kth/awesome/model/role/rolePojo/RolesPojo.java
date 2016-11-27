package se.kth.awesome.model.role.rolePojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.security.core.GrantedAuthority;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolesPojo implements Serializable, Comparable<RolesPojo>,GrantedAuthority {



	private String authority;


	public RolesPojo() {

	}

	public RolesPojo(Role theRoles) {
		this.authority = theRoles.getAuthority();
		this.id = (long) (theRoles.ordinal()+1);

	}


	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
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

		if (authority != rolesPojo.authority) return false;
		return id != null ? id.equals(rolesPojo.id) : rolesPojo.id == null;
	}

	@Override
	public int hashCode() {
		int result = authority != null ? authority.hashCode() : 0;
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
