package se.kth.awesome.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.kth.awesome.security.RestAuthenticationEntryPoint;
import se.kth.awesome.security.auth.ajax.AjaxAuthenticationProvider;
import se.kth.awesome.security.auth.ajax.AjaxLoginProcessingFilter;
import se.kth.awesome.security.auth.jwt.JwtAuthenticationProvider;
import se.kth.awesome.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import se.kth.awesome.security.auth.jwt.SkipPathRequestMatcher;
import se.kth.awesome.security.auth.jwt.extractor.TokenExtractor;

/**
 * WebSecurityConfig
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/social/login";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/social/**";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/authorization/token";
    
    @Autowired private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired private AuthenticationFailureHandler failureHandler;
    @Autowired private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired private TokenExtractor tokenExtractor;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private AuthenticationSuccessHandler successHandler;
    @Autowired private AjaxAuthenticationProvider ajaxAuthenticationProvider;
    @Autowired private ObjectMapper objectMapper;

    @Bean
    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
        List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
        JwtTokenAuthenticationProcessingFilter filter 
            = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable() // We don't need CSRF for JWT based authentication
        .exceptionHandling()
        .authenticationEntryPoint(this.authenticationEntryPoint)
        
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
            .authorizeRequests()
                .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll() // Login end-point
                .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token refresh end-point
                .antMatchers("/console/*").permitAll() // H2 Console Dash-board - only for testing
                .antMatchers("/test/*").permitAll() // test if the server works in Tomcat WTF
                .antMatchers("/social/register").permitAll()
                .antMatchers("/social/userSearch/*").permitAll()
        .and()
            .authorizeRequests()
                .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected API End-points
        .and()
            .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
