package se.kth.awesome.model.security;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * UserRole
 *
 */
@Entity
@Table(name = "user_role")
public class UserRole {

    @EmbeddedId
    Id id = new Id();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", insertable=false, updatable=false)
    protected Role role;

    public Role getRole() {
        return role;
    }


    @Embeddable
    public static class Id implements Serializable {
        private static final long serialVersionUID = 1322120000551624359L;
        public Id() { }

        public Id(Long userId, Role role) {
            this.userId = userId;
            this.role = role;
        }

        // TODO change column name from app_user_id to Role_id
        protected Long userId;
        @Column(name = "app_user_id")
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        protected Role role;
        @Enumerated(EnumType.STRING)
        @Column(name = "role")
        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }
    }
    

}
