package se.kth.awesome.security.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import se.kth.awesome.SpringbootSecurityJwtApplication;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.security.auth.JwtAuthenticationToken;
import se.kth.awesome.security.auth.jwt.model.token.JwtSettings;
import se.kth.awesome.security.auth.jwt.model.token.JwtToken;
import se.kth.awesome.security.auth.jwt.model.token.RawAccessJwtToken;

import static se.kth.awesome.util.Util.nLin;

/**
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of {@link JwtToken} to perform authentication.
 * 
 * @author vladimir.stankovic
 *
 * Aug 5, 2016
 */
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;
    private final Logger logger2 = LoggerFactory.getLogger(getClass());
    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger2.error(nLin+nLin+""+ SpringbootSecurityJwtApplication.steps++ +" ---------- JwtAuthenticationProvider.authenticate ----------\n");
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();
        List<String> scopes = jwsClaims.getBody().get(jwtSettings.getClaimKeyRoles(), List.class);
        List<GrantedAuthority> authorities = scopes.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
        UserPojo userPojo = new UserPojo(subject, null, null);
//        UserPojo context = UserContext.create(subject, authorities);
        
        return new JwtAuthenticationToken(userPojo, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
