package se.kth.awesome.model.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.role.Role;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL )
public class RoleIdPojo {
	public Long userId;


	public Role role;

	public RoleIdPojo() { }

	public RoleIdPojo(Long userId, Role role) {
		this.userId = userId;
		this.role = role;
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
