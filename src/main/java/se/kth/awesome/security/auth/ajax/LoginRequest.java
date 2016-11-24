package se.kth.awesome.security.auth.ajax;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model intended to be used for AJAX based authentication.
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL )
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest( String username,  String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
