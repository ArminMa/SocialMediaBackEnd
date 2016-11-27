package se.kth.awesome.security.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import se.kth.awesome.SpringbootSecurityJwtApplication;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.security.exceptions.AuthMethodNotSupportedException;

import static se.kth.awesome.util.Util.nLin;

/**
 * AjaxLoginProcessingFilter
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static Logger logger = LoggerFactory.getLogger(AjaxLoginProcessingFilter.class);
	private final Logger logger2 = LoggerFactory.getLogger(getClass());

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;
    
    public AjaxLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler, 
            AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {
        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        logger2.error(nLin+nLin+""+ SpringbootSecurityJwtApplication.steps++ +" ---------- AjaxLoginProcessingFilter.attemptAuthentication ----------\n");
//        logger2.error( "\n"+token +"\n");
//        logger2.error(nLin+nLin+" ---------- AjaxAuthenticationProvider.attemptAuthentication  ----------\n");

        if (!HttpMethod.POST.name().equals(request.getMethod()) /*|| !WebUtil.isAjax(request)*/) {
            if(logger.isDebugEnabled()) {
                logger.debug("Authentication method not supported. Request method: " + request.getMethod());
            }
            throw new AuthMethodNotSupportedException("Authentication method not supported");
        }


//        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        UserPojo loginRequest = objectMapper.readValue(request.getReader(), UserPojo.class);
	    logger2.error(nLin+nLin+"---------- AjaxLoginProcessingFilter.attemptAuthentication debug start ----------\n" +
			    "\n"+ loginRequest.toString() +"\n" +
			    "\n ---------- AjaxLoginProcessingFilter.attemptAuthentication debug end ----------\n\n");

        if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("Username or Password not provided");
        }



        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
