package se.kth.awesome.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.security.auth.jwt.extractor.TokenExtractor;
import se.kth.awesome.security.auth.jwt.verifier.TokenVerifier;
import se.kth.awesome.security.auth.jwt.model.token.JwtSettings;
import se.kth.awesome.security.config.WebSecurityConfig;
import se.kth.awesome.security.exceptions.InvalidJwtToken;
import se.kth.awesome.security.auth.jwt.model.token.JwtToken;
import se.kth.awesome.security.auth.jwt.model.token.JwtTokenFactory;
import se.kth.awesome.security.auth.jwt.model.token.RawAccessJwtToken;
import se.kth.awesome.security.auth.jwt.model.token.RefreshToken;
import se.kth.awesome.service.UserEntityService;

/**
 * RefreshTokenEndpoint
 * 
 * @author vladimir.stankovic
 *
 * Aug 17, 2016
 */
@RestController
public class RefreshTokenEndpoint {
    @Autowired private JwtTokenFactory tokenFactory;
    @Autowired private JwtSettings jwtSettings;
    @Autowired private UserEntityService userService;
    @Autowired private TokenVerifier tokenVerifier;
    @Autowired @Qualifier("jwtHeaderTokenExtractor") private TokenExtractor tokenExtractor;
    
    @RequestMapping(value="/api/auth/token", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));
        
        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());

        String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }

        String subject = refreshToken.getSubject();
        UserPojo user = userService.findByUsername(subject);
        if(user== null)
            throw  new UsernameNotFoundException("user not found: " + subject);

        if (user.getAuthorities() == null) throw new InsufficientAuthenticationException("user has no roles assigned");
//        List<GrantedAuthority> authorities = user.getAuthorities().stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().getAuthority().authority()))
//                .collect(Collectors.toList());
//
//        UserContext userContext = UserContext.create(user.getUsername(), authorities);

        return tokenFactory.createAccessJwtToken(user);
    }
}
