package se.kth.awesome.model.role;


import org.springframework.security.core.GrantedAuthority;
import se.kth.awesome.model.user.UserEntity;
import se.kth.awesome.model.user.UserPojo;

/**
 * Enumerated {@link UserEntity} roles.
 * Enumerated {@link UserPojo} roles.
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public enum Role implements GrantedAuthority {
    MEMBER, PREMIUM_MEMBER, ADMIN, SUPER_ADMIN , ANONYMOUS, REFRESH_TOKEN;
    
    public String authority() {
        return "ROLE_" + this.name();
    }
    public String getAuthority() {
        return "ROLE_" + this.name();
    }

}
