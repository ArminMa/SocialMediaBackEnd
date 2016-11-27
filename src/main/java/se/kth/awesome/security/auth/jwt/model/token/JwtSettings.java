package se.kth.awesome.security.auth.jwt.model.token;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "awesome.security.jwt")
public class JwtSettings {
    /**
     * {@link JwtToken} will expire after this time.
     */
    private Integer tokenExpirationTime;

    /**
     * Token issuer.
     */
    private String tokenIssuer;
    
    /**
     * Key is used to sign {@link JwtToken}.
     */
    private String tokenSigningKey;
    
    /**
     * {@link JwtToken} can be refreshed during this timeframe.
     */
    private Integer refreshTokenExpTime;

	/**
	 * Map Key for ISSUER in token claims  {@link JwtToken}.
	 */
    private String claimKeyIssuer;

	/**
	 * Map Key for ? in token claims  {@link JwtToken}.
	 */
    private String claimKeySubject;
    private String claimKeyAudience;
    private String claimKeyCreated;
    private String claimKeyPayload;
    private String claimKeyRoles;
	private String audienceUnknown;
	private String audienceWeb;
	private String audienceMobile;
	private String audienceTablet;
	private Integer creationBufferSeconds; // 5 min


	public String getClaimKeyIssuer() {
		return claimKeyIssuer;
	}
	public void setClaimKeyIssuer(String claimKeyIssuer) {
		this.claimKeyIssuer = claimKeyIssuer;
	}

	public String getClaimKeySubject() {
		return claimKeySubject;
	}
	public void setClaimKeySubject(String claimKeySubject) {
		this.claimKeySubject = claimKeySubject;
	}

	public String getClaimKeyAudience() {
		return claimKeyAudience;
	}
	public void setClaimKeyAudience(String claimKeyAudience) {
		this.claimKeyAudience = claimKeyAudience;
	}

	public String getClaimKeyCreated() {
		return claimKeyCreated;
	}
	public void setClaimKeyCreated(String claimKeyCreated) {
		this.claimKeyCreated = claimKeyCreated;
	}

	public String getClaimKeyPayload() {
		return claimKeyPayload;
	}
	public void setClaimKeyPayload(String claimKeyPayload) {
		this.claimKeyPayload = claimKeyPayload;
	}

	public String getClaimKeyRoles() {
		return claimKeyRoles;
	}
	public void setClaimKeyRoles(String claimKeyRoles) {
		this.claimKeyRoles = claimKeyRoles;
	}

	public String getAudienceUnknown() {
		return audienceUnknown;
	}
	public void setAudienceUnknown(String audienceUnknown) {
		this.audienceUnknown = audienceUnknown;
	}

	public String getAudienceWeb() {
		return audienceWeb;
	}
	public void setAudienceWeb(String audienceWeb) {
		this.audienceWeb = audienceWeb;
	}

	public String getAudienceMobile() {
		return audienceMobile;
	}
	public void setAudienceMobile(String audienceMobile) {
		this.audienceMobile = audienceMobile;
	}

	public String getAudienceTablet() {
		return audienceTablet;
	}
	public void setAudienceTablet(String audienceTablet) {
		this.audienceTablet = audienceTablet;
	}

	public Integer getCreationBufferSeconds() {
		return creationBufferSeconds;
	}
	public void setCreationBufferSeconds(Integer creationBufferSeconds) {
		this.creationBufferSeconds = creationBufferSeconds;
	}

	public Integer getRefreshTokenExpTime() {
        return refreshTokenExpTime;
    }
    public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public Integer getTokenExpirationTime() {
        return tokenExpirationTime;
    }
    public void setTokenExpirationTime(Integer tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }
    
    public String getTokenIssuer() {
        return tokenIssuer;
    }
    public void setTokenIssuer(String tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }
    
    public String getTokenSigningKey() {
        return tokenSigningKey;
    }
    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }
}
