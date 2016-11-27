package se.kth.awesome.security.util.JWT;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.security.Key;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.mobile.device.Device;
import se.kth.awesome.model.user.UserPojo;

/**
 * Created by Sys on 2016-08-01.
 */
public class TokenIoUtils implements Serializable {


	private static final Long TOKEN_DURATION_SECONDS = (60L * 60L * 24L * 7L); // 1 week
	private static final Long TOKEN_CREATION_BUFFER_SECONDS = (60L * 5L); // 5 min
	private static final String tokenIssuer = "http://socialWar.kth";

	private static final String CLAIM_KEY_ISSUER = "issuer";
	private static final String CLAIM_KEY_SUBJECT = "sub";
	private static final String CLAIM_KEY_AUDIENCE = "audience";
	private static final String CLAIM_KEY_CREATED = "created";
	private static final String CLAIM_KEY_PAYLOAD = "payload";
	private static final String CLAIM_KEY_ROLES = "roles";

	private static final String AUDIENCE_UNKNOWN = "unknown";
	private static final String AUDIENCE_WEB = "web";
	private static final String AUDIENCE_MOBILE = "mobile";
	private static final String AUDIENCE_TABLET = "tablet";







//	@Value("${server.secretKey}")
//	public static String serverSecretKey;

//	@Value("${token.header}")
//	private static String tokenHeader;



	public String createJwtHS512(String username, String secretKey) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuer(tokenIssuer)
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(Date.from(Instant.now()))
				.setExpiration(Date.from(Instant.now().plusSeconds(TOKEN_DURATION_SECONDS)))
				.setNotBefore(Date.from(Instant.now().minusSeconds(TOKEN_CREATION_BUFFER_SECONDS)))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	public static String createJwtHS256(
			String issuer,
			String audience,
			String subject,
			List<String> roles,
			String jsonPayload,
			Key key) {

		return generateToken(generateClaims( issuer, audience, subject, roles, jsonPayload), key);
	}


	// used whit Key also
	public static Map<String, Object> generateClaims(
			String issuer,
			String audience,
			String subject,
			List<String> roles,
			String jsonPayload
			) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_ISSUER, issuer);
		claims.put(CLAIM_KEY_SUBJECT, subject);
		claims.put(CLAIM_KEY_AUDIENCE, audience);
		claims.put(CLAIM_KEY_CREATED, new Date());
		claims.put(CLAIM_KEY_ROLES, roles);
		claims.put(CLAIM_KEY_PAYLOAD,jsonPayload);
		return claims;
	}



	private static String generateToken(Map<String, Object> claims, Key key) {
		return Jwts
				.builder()
				.setClaims(claims)
				.setExpiration(addDays( new Date(), 7))
				.setNotBefore(Date.from(Instant.now().minusSeconds(TOKEN_CREATION_BUFFER_SECONDS)))
				.setIssuedAt(new Date(System.nanoTime()))
				.setId(UUID.randomUUID().toString())
				.signWith(SignatureAlgorithm.HS256, key)
				.compact();
	}


	public static String getPayloadFromJwt(String token, Key key) {

		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey(key)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			return null;
		}

		return (String) claims.get(CLAIM_KEY_PAYLOAD);

	}


	public static boolean isValid(String token, Key key) {
		try {
//			Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
			Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public static String getSubject(String jwsToken, Key key) {
		if (isValid(jwsToken, key)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
			return claimsJws.getBody().getSubject();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<String> getRoles(String jwsToken, Key key) {
		if (isValid(jwsToken, key)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
			return (List<String>) claimsJws.getBody().get(CLAIM_KEY_ROLES);
		}
		return null;
	}

	public static int getVersion(String jwsToken, Key key) {
		if (isValid(jwsToken, key)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
			return Integer.parseInt(claimsJws.getBody().getId());
		}
		return -1;
	}

	public static Date getCreatedDateFromToken(String token, Key key) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token, key);
			created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	public static Date getExpirationDateFromToken(String token, Key key) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token, key);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	public static String getAudienceFromToken(String token, Key key) {
		String audience;
		try {
			final Claims claims = getClaimsFromToken(token, key);
			audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}



	public static Boolean canTokenBeRefreshed(String token, Date lastPasswordReset, Key key) {
		final Date created = getCreatedDateFromToken(token, key);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token, key) || ignoreTokenExpiration(token, key));
	}

	public static String refreshToken(String token, Key key) {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token, key);
			claims.put(CLAIM_KEY_CREATED, new Date());


			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = generateToken(claims, key);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	public static Boolean validateToken(String token, UserPojo userDetails, Key key) {
		return  !isTokenExpired(token, key) && getAudienceFromToken(token, key).equals(userDetails.getEmail());
	}



	/**
	 *
	 * 	To add one day, per the question asked, call it as follows:
	 *
	 * 	String sourceDate = "2016-08-09 15:48:57.596";
	 * 	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	 * 	Date myDate = format.parse(sourceDate);
	 * 	myDate = DateUtil.addDays(myDate, 1);
	 *
	 *
	 * @param date the date that is going to be manipulated
	 * @param days is number of days that is going to be added to date
	 * @return java.util.Date that is the  @param java.util.Date date + @param int days (number of day to be added)
	 */
	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime( date );
		cal.add(Calendar.DATE, days); //minus number would decrement the days
		return cal.getTime();
	}

		private static Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private static Claims getClaimsFromToken(String token, Key key) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey(key)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}

		return claims;
	}

	private static Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + TOKEN_DURATION_SECONDS );
	}

	private static Boolean isTokenExpired(String token, Key key) {
		final Date expiration = getExpirationDateFromToken( token, key );
		return expiration.before(new Date());
	}

	private static Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private static String generateAudience(Device device) {
		String audience = AUDIENCE_UNKNOWN;
		if (device.isNormal()) {
			audience = AUDIENCE_WEB;
		} else if (device.isTablet()) {
			audience = AUDIENCE_TABLET;
		} else if (device.isMobile()) {
			audience = AUDIENCE_MOBILE;
		}
		return audience;
	}

	private static Boolean ignoreTokenExpiration(String token, Key key) {
		String audience = getAudienceFromToken(token, key);
		return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
	}

}
