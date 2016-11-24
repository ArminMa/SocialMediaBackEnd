package se.kth.awesome.security.auth.ajax;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import se.kth.awesome.SpringbootSecurityJwtApplication;
import se.kth.awesome.model.User.UserPojo;
import se.kth.awesome.security.model.UserContext;
import se.kth.awesome.security.util.PasswordSaltUtil;
import se.kth.awesome.service.UserEntityService;

import static se.kth.awesome.util.Util.nLin;


/**
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserEntityService userService;
	@Value("${shared.secretKey}")
	public String serverSecretKey;
	private static PasswordSaltUtil passwordSaltUtil = new PasswordSaltUtil();
	public static final Logger logger1 = LoggerFactory.getLogger( AjaxAuthenticationProvider.class );
	private final Logger logger2 = LoggerFactory.getLogger(getClass());

	public AjaxAuthenticationProvider(BCryptPasswordEncoder saltUtil) {}

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	    logger2.error(nLin+nLin+"   "+ SpringbootSecurityJwtApplication.steps++ +" ---------- AjaxAuthenticationProvider.authenticate debug start ----------\n" );

        Assert.notNull(authentication, "No authentication data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserPojo user = userService.findByUsername(username);



        if(user == null)
            throw new UsernameNotFoundException("User not found: " + username);




	    String password2 = passwordSaltUtil.encodePassword( password, serverSecretKey );

	    logger2.error(nLin+nLin+"---------- AjaxAuthenticationProvider.authenticate debug start ----------\n" +
			    "\n"+ user.toString() +"\n" +
			    "\npassword = "+ password +"\n" +
			    "\npassword2 = "+ password2 +"\n" +
			    "\n ---------- AjaxAuthenticationProvider.authenticate debug end ----------\n");

        if (password2 == null || !password2.equals(user.getPassword()) ) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }

        if (user.getAuthorities() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
        
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().getRole().authority()))
                .collect(Collectors.toList());
        
        UserContext userContext = UserContext.create(user.getUsername(), authorities);
        
        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }




}
