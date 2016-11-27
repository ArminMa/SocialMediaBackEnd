package se.kth.awesome.security.auth.jwt.model.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.security.exceptions.JwtExpiredTokenException;


/**
 * RefreshToken
 * 
 * @author vladimir.stankovic
 *
 * Aug 19, 2016
 */
@SuppressWarnings("unchecked")
public class RefreshToken implements JwtToken {
    private Jws<Claims> claims;
    @Autowired private JwtSettings jwtSettings;

    private static String roleMapKey;
    private RefreshToken(Jws<Claims> claims) {
        roleMapKey = jwtSettings.getClaimKeyRoles();
        this.claims = claims;
    }

    /**
     * Creates and validates Refresh token 
     * 
     * @param token
     * @param signingKey
     * 
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     * 
     * @return
     */
    public static Optional<RefreshToken> create(RawAccessJwtToken token, String signingKey) {
        Jws<Claims> claims = token.parseClaims(signingKey);

        List<String> roles = claims.getBody().get(roleMapKey, List.class);
        if (roles == null || roles.isEmpty()
                || !roles.stream().filter(role -> Role.REFRESH_TOKEN.authority().equals(role)).findFirst().isPresent()) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(claims));
    }

    @Override
    public String getToken() {
        return null;
    }

    public Jws<Claims> getClaims() {
        return claims;
    }
    
    public String getJti() {
        return claims.getBody().getId();
    }
    
    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
