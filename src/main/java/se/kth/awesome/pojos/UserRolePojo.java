package se.kth.awesome.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.model.security.Role;
import se.kth.awesome.model.security.UserRole;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@JsonInclude(JsonInclude.Include.NON_NULL )
public class UserRolePojo {


    Id id = new Id();

    protected Role role;

    public Role getRole() {
        return role;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
    @JsonInclude(JsonInclude.Include.NON_NULL )
    public static class Id implements Serializable {
        protected Role role;
        protected Long userId;

        public Id() {

        }
        public Id(Long userId, Role role) {
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

}
