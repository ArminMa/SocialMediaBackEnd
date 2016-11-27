package se.kth.awesome.security.auth.jwt.model.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Component;
import se.kth.awesome.model.role.Role;
import se.kth.awesome.model.role.rolePojo.UserRolePojo;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.security.AwesomeServerKeys;
import se.kth.awesome.security.auth.ajax.AjaxAuthenticationProvider;
import se.kth.awesome.security.util.CipherUtils;
import se.kth.awesome.security.util.KeyUtil;


import static se.kth.awesome.util.Util.nLin;

/**
 * Factory class that should be always used to create {@link JwtToken}.
 *
 * @author vladimir.stankovic
 *
 * May 31, 2016
 */


@Component
public class JwtTokenFactory {
	private final JwtSettings settings;
	private final AwesomeServerKeys awesomeServerKeys;
	public static final Logger logger1 = LoggerFactory.getLogger( AjaxAuthenticationProvider.class );



	private SecretKey secretKey ;

	@Autowired
	public JwtTokenFactory(JwtSettings settings, AwesomeServerKeys awesomeServerKeys) {
		this.settings = settings;
		this.awesomeServerKeys = awesomeServerKeys;
	}



	/**
	 *
	 * @param userContext dfg
	 * @return AccessJwtToken
	 */
	public AccessJwtToken createAccessJwtToken(UserPojo userContext) {
		secretKey = KeyUtil.SymmetricKey.getSecretKeyFromString(awesomeServerKeys.getEncryptPayloadKey());
		if (StringUtils.isBlank(userContext.getUsername()))
			throw new IllegalArgumentException("Cannot create JWT Token without username");

		if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
			throw new IllegalArgumentException("user doesn't have any privileges");
		DateTime currentTime = new DateTime();
		Claims claims = generateClaims(userContext, currentTime);



		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuer(settings.getTokenIssuer())
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(currentTime.toDate())
				.setExpiration(currentTime.plusHours(settings.getTokenExpirationTime()).toDate())
//				.setNotBefore(currentTime.minusSeconds(settings.getCreationBufferSeconds()).toDate())
//				.setPayload(userContext.toString())
				.signWith(SignatureAlgorithm.HS256, settings.getTokenSigningKey())
				.compact();

		return new AccessJwtToken(token, claims);
	}

	public JwtToken createRefreshToken(UserPojo userContext) {
		if (StringUtils.isBlank(userContext.getUsername())) {
			throw new IllegalArgumentException("Cannot create JWT Token without username");
		}

		DateTime currentTime = new DateTime();
		userContext.setAuthorities(Arrays.asList(new UserRolePojo(Role.REFRESH_TOKEN)));
		Claims claims = generateClaims(userContext, currentTime);

		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuer(settings.getTokenIssuer())
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(currentTime.toDate())
				.setExpiration(currentTime.plusDays(settings.getRefreshTokenExpTime()).toDate())
//				.setNotBefore(currentTime.minusSeconds(settings.getCreationBufferSeconds()).toDate())
//				.setPayload(userContext.toString())
				.signWith(SignatureAlgorithm.HS256, settings.getTokenSigningKey())
				.compact();

		return new AccessJwtToken(token, claims);
	}


	public  Claims generateClaims(UserPojo userPojo, DateTime currentTime) {

		Claims claims = Jwts.claims().setSubject(userPojo.getUsername());
		claims.put(settings.getClaimKeyRoles(), userPojo.getAuthorities().stream().map(UP -> UP.getAuthority().getAuthority()).collect(Collectors.toList()));
		claims.put(settings.getClaimKeyIssuer(), settings.getTokenIssuer());
		claims.put(settings.getClaimKeySubject(), userPojo.getUsername());
//		claims.put(settings.getClaimKeyAudience(), audience);
		claims.put(settings.getClaimKeyCreated(), currentTime.toDate());


		userPojo.setPassword(UUID.randomUUID().toString());
		String encyptedUser = CipherUtils.encryptWithSymmetricKey(userPojo.toString(), secretKey );
		if(encyptedUser.isEmpty()) return null;
		claims.put(settings.getClaimKeyPayload(), encyptedUser);
		return claims;
	}
















	// --------------- functions that is going to be needed later ---------------------

	public  String createJwtHS256(UserPojo userPojo ) {
		DateTime currentTime = new DateTime();
		Claims claims = Jwts.claims().setSubject(userPojo.getUsername());
		claims.put(settings.getClaimKeyRoles(), userPojo.getAuthorities().stream().map(UP -> UP.getAuthority().getAuthority()).collect(Collectors.toList()));
		claims.put(settings.getClaimKeyIssuer(), settings.getTokenIssuer());
		claims.put(settings.getClaimKeySubject(), userPojo.getUsername());
//		claims.put(settings.getClaimKeyAudience(), audience);
		claims.put(settings.getClaimKeyCreated(), currentTime.toDate());
//		claims.put(settings.getClaimKeyRoles(), roles);

		//set unwanted variables to null
		userPojo.setPassword(null);
		userPojo.setFriendRequests(null);
		userPojo.setChatMessages(null);
		userPojo.setMailMessages(null);
		userPojo.setPicture(null);
		userPojo.setLog(null);
		claims.put(settings.getClaimKeyPayload(),userPojo.toString());


		return Jwts.builder()
				.setClaims(claims)
				.setIssuer(settings.getTokenIssuer())
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(currentTime.toDate())
				.setExpiration((currentTime.plusHours(settings.getRefreshTokenExpTime())).toDate())
				.setNotBefore(currentTime.minusSeconds(settings.getCreationBufferSeconds()).toDate())
				.signWith(SignatureAlgorithm.HS256, settings.getTokenSigningKey())
				.compact();

	}







	private  String generateToken(Claims claims ) {
		DateTime currentTime = new DateTime();
		return Jwts.builder()
				.setClaims(claims)
				.setIssuer(settings.getTokenIssuer())
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(currentTime.toDate())
				.setExpiration(currentTime.plusHours(settings.getRefreshTokenExpTime()).toDate())
				.setNotBefore(currentTime.minusSeconds(settings.getCreationBufferSeconds()).toDate())
				.signWith(SignatureAlgorithm.HS256,  settings.getTokenSigningKey())
				.compact();
	}


	public  String getPayloadFromJwt(String token) {

		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey( settings.getTokenSigningKey())
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			System.out.println("JwtTokenFactory.getPayloadFromJwt Exception and return null");
			return null;
		}
		System.out.println(nLin+nLin+ " JwtTokenFactory.getPayloadFromJwt claims.get(settings.getClaimKeyPayload() = " + claims.get(settings.getClaimKeyPayload()) + nLin);
		return (String) claims.get(settings.getClaimKeyPayload(), String.class);

	}

	public  boolean isValid(String token ) {
		if(isTokenExpired(token)) return false;
		try {
			Jwts.parser().setSigningKey( settings.getTokenSigningKey()).parseClaimsJws(token.trim());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public  String getSubject(String jwsToken ) {
		if (isValid(jwsToken)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey( settings.getTokenSigningKey()).parseClaimsJws(jwsToken);
			return claimsJws.getBody().getSubject();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public  List<String> getRoles(String jwsToken ) {
		if (isValid(jwsToken)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey( settings.getTokenSigningKey()).parseClaimsJws(jwsToken);
			return (List<String>) claimsJws.getBody().get(settings.getClaimKeyRoles());
		}
		return null;
	}

	public  int getVersion(String jwsToken ) {
		if (isValid(jwsToken)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey( settings.getTokenSigningKey()).parseClaimsJws(jwsToken);
			return Integer.parseInt(claimsJws.getBody().getId());
		}
		return -1;
	}

	public  Date getCreatedDateFromToken(String token ) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = new Date((Long) claims.get(settings.getClaimKeyCreated()));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	public  Date getExpirationDateFromToken(String token ) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	public  String getAudienceFromToken(String token ) {
		String audience;
		try {
			final Claims claims = getClaimsFromToken(token);
			audience = (String) claims.get(settings.getClaimKeyAudience());
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}



	public  Boolean canTokenBeRefreshed(String token, Date lastPasswordReset ) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public  Boolean validateToken(String token, UserPojo userDetails ) {
		return  !isTokenExpired(token) && getAudienceFromToken(token).equals(userDetails.getEmail());
	}

	private  Claims getClaimsFromToken(String token ) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey( settings.getTokenSigningKey())
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}

		return claims;
	}

	private  Boolean isTokenExpired(String token ) {
		DateTime currentTime = new DateTime();
		final Date expiration = getExpirationDateFromToken( token );

		boolean before = expiration.before(currentTime.toDate());
		boolean after = expiration.after(currentTime.toDate());
		System.out.println(nLin+"expiration = " +expiration);
		System.out.println("currentTime.toDate() = " +currentTime.toDate());
		System.out.println("before = " +before + " , after = " + after +nLin);

		return expiration.before(currentTime.toDate());
	}

	private  Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private  String generateAudience(Device device) {
		String audience = settings.getAudienceUnknown();
		if (device.isNormal()) {
			audience = settings.getAudienceWeb();
		} else if (device.isTablet()) {
			audience = settings.getAudienceTablet();
		} else if (device.isMobile()) {
			audience = settings.getAudienceMobile();
		}
		return audience;
	}

	private  Boolean ignoreTokenExpiration(String token ) {
		String audience = getAudienceFromToken(token);
		return (settings.getAudienceTablet().equals(audience) || settings.getAudienceMobile().equals(audience));
	}










}
