package se.kth.awesome.model.role;


import se.kth.awesome.model.User.UserEntity;
import se.kth.awesome.model.User.UserPojo;

/**
 * Enumerated {@link UserEntity} roles.
 * Enumerated {@link UserPojo} roles.
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public enum Role {
    MEMBER, PREMIUM_MEMBER, ADMIN, SUPER_ADMIN , ROLE_ANONYMOUS;
    
    public String authority() {
        return "ROLE_" + this.name();
    }
}
