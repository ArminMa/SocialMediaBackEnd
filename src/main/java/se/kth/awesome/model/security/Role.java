package se.kth.awesome.model.security;

/**
 * Enumerated {@link se.kth.awesome.model.UserEntity} roles.
 *
 */
public enum Role {
    ADMIN, PREMIUM_MEMBER, MEMBER;
    
    public String authority() {
        return "role_" + this.name();
    }
}
