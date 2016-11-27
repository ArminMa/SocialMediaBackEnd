package se.kth.awesome.security.util.JWT;


import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.base64url.Base64;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.EcJwkGenerator;
import org.jose4j.jwk.EllipticCurveJsonWebKey;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.AesKey;
import org.jose4j.keys.EllipticCurves;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Component;

import static org.jose4j.jwk.PublicJsonWebKey.Factory.newPublicJwk;

@Component
public class TokenJose4jUtils {

	public static class EllipticJWT {

		/**
		 * @param issue                     String
		 * @param audience                  String
		 * @param subject                   String
		 * @param senderPrivateEllipticJwt  String
		 * @param receiverPublicEllipticJwt String
		 * @param jsonPayload               String
		 * @return String JWT
		 */
		public static String ProduceJWTVerifyAndEncrypt(
				String issue,
				String audience,
				String subject,
				String senderPrivateEllipticJwt,
				String receiverPublicEllipticJwt,
				String jsonPayload) throws JoseException {

			// the keys in
			EllipticCurveJsonWebKey senderJWK = null;
			try {
				senderJWK = JsonWebKeyUtil.getEllipticJwkFromJson(senderPrivateEllipticJwt);
			} catch (JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}

			// Create the Claims, which will be the content of the JWT
			JwtClaims claim = produceClaim(issue, audience, subject, jsonPayload);

			// A JWT is a JWS and/or a JWE with JSON claims as the payload.
			// In this example it is a JWS nested inside a JWE
			// So we first create a JsonWebSignature object.
			JsonWebSignature jws = new JsonWebSignature();

			// The payload of the JWS is JSON content of the JWT Claims
			jws.setPayload(claim.toJson());

			// The JWT is signed using the sender's private key
			jws.setKey(senderJWK.getPrivateKey());


			// Set the Key ID (kid) header because it's just the polite thing to do.
			// We only have one signing key in this example but a using a Key ID helps
			// facilitate a smooth key rollover process
			jws.setKeyIdHeaderValue(senderJWK.getKeyId());
			jws.setContentTypeHeaderValue("JWS");

			// Set the signature algorithm on the JWT/JWS that will integrity protect the claims
			jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);

			// Sign the JWS and produce the compact serialization, which will be the inner JWT/JWS
			// representation, which is a string consisting of three dot ('.') separated
			// base64url-encoded parts in the form Header.Payload.Signature
			String innerJwt = null;
			try {
				innerJwt = jws.getCompactSerialization();
			} catch (JoseException e) {
				e.printStackTrace();
			}


			return securJWT(innerJwt, receiverPublicEllipticJwt);


			// Now you can do something with the JWT. Like send it to some other party
			// over the clouds and through the interwebs.

		}


		public static String securJWT(String payload, String receiverPublicEllipticJwt) throws JoseException {

			// the keys in

			String jwt = null;

			EllipticCurveJsonWebKey receiverJWK = null;
			try {
				receiverJWK = JsonWebKeyUtil.getEllipticJwkFromJson(receiverPublicEllipticJwt);
			} catch (JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}

			// The outer JWT is a JWE
			JsonWebEncryption jwe = new JsonWebEncryption();

			// The output of the ECDH-ES key agreement will encrypt a randomly generated content encryption key
			jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.ECDH_ES_A128KW);

			// The content encryption key is used to encrypt the payload
			// with a composite AES-CBC / HMAC SHA2 encryption algorithm
			String encAlg = ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256;
			jwe.setEncryptionMethodHeaderParameter(encAlg);

			// We encrypt to the receiver using their public key
			jwe.setKey(receiverJWK.getPublicKey());
			jwe.setKeyIdHeaderValue(receiverJWK.getKeyId());

			// A nested JWT requires that the cty (Content Type) header be set to "JWT" in the outer JWT
			jwe.setContentTypeHeaderValue("JWT");

			// The inner JWT is the payload of the outer JWT
			jwe.setPayload(payload);

			// Produce the JWE compact serialization, which is the complete JWT/JWE representation,
			// which is a string consisting of five dot ('.') separated
			// base64url-encoded parts in the form Header.EncryptedKey.IV.Ciphertext.AuthenticationTag


			try {
				jwt = jwe.getCompactSerialization();
			} catch (JoseException e) {
				e.printStackTrace();
			}

			return jwt;


		}


		/***************************RECEIVER'S END ***********************************/

		public static String getPayloadCurveJWK(String issue,
		                                        String audience,
		                                        String senderPrivateEllipticJwt,
		                                        String receiverPublicEllipticJwt,
		                                        String token) {
			try {

				JwtClaims jwtClaims = getJwtClaimVerifyAndDecrypt(issue,
						audience,
						senderPrivateEllipticJwt,
						receiverPublicEllipticJwt,
						token);


				return (String) jwtClaims.getClaimValue("payload");
			} catch ( JoseException e ) {
				e.printStackTrace();
				return null;
			}


		}

	}


	//------------------------------- SymmetricJWT.class start -------------------------------

	public static class SymmetricJWT {


		public static JwtClaims validateTokenAndProcessClaims(final Key key,
		                                                      final String issuer,
		                                                      final String audience,
		                                                      final String subject,
		                                                      final String token) throws JoseException {

			final JwtConsumer jwtConsumer = new JwtConsumerBuilder()
					.setRequireExpirationTime() // the JWT must have an expiration time
					.setAllowedClockSkewInSeconds(180) // allow some leeway in validating time based claims to account for clock skew
					.setExpectedIssuer(issuer) // whom the JWT needs to have been issued by
					.setExpectedAudience(audience) // to whom the JWT is intended for
					.setExpectedSubject(subject)
					.setDecryptionKey(key)
					.setEnableRequireEncryption()
					.setDisableRequireSignature()
					.setSkipSignatureVerification()
					.build(); // create the JwtConsumer instance

			//  Validate the JWT and process it to the Claims
			try {
				return jwtConsumer.processToClaims(token);
			} catch (InvalidJwtException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}
		}

		public static Map<String, String> getJwtPayloadList(
				final Key key,
				final String issuer,
				final String audience,
				final String subject,
				final List<String> payloadKey,
				final String token) throws InvalidJwtException, MalformedClaimException, JoseException {


			JwtClaims claims = null;
			try {
				claims = validateTokenAndProcessClaims(
						key,
						issuer,
						audience,
						subject,
						token
				);

				Map<String, String> listOfPayload = new HashMap<>();
				for (String K : payloadKey) {
					listOfPayload.put(K, (String) claims.getClaimValue(K));
				}

				return listOfPayload;

			} catch (JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}


		}

		public static String getJwtPayload(
				final SecretKey key,
				final String issuer,
				final String audience,
				final String subject,
				final String payloadKey,
				final String token) throws JoseException {

			JwtClaims claims = null;
			try {
				claims = validateTokenAndProcessClaims(
						key,
						issuer,
						audience,
						subject,
						token
				);
				return (String) claims.getClaimValue(payloadKey);
			} catch (JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}



		}


		/**
		 * Convenience method that generates a JWT Token given a set of parameters common to JWT
		 * implementations.
		 *
		 * @param key                              The key key
		 * @param issuer                           The indended Issuer of the generated token
		 * @param audience                         The intended Audience of the generated token
		 * @param subject                          The indended Subject of the generated token
		 * @param payload                          JSON snippet that will be inserted into the claim under the
		 *                                         key 'payload'
		 *                                         Map<String , List<String>> sendPayload = new HashMap<>( );
		 *                                         sendPayload.put("payload", Arrays.asList(faceuserPojo.toString()));
		 * @param expirationTimeMinutesInTheFuture The maximum number of minutes this generated token is valid.
		 * @return JWT token string of the form string.string.string
		 * @throws JoseException if any issue occurs during generation. Mostly likely a key
		 *                       issue.
		 */
		public static String generateJWT(final SecretKey key,
		                                 final String issuer,
		                                 final String audience,
		                                 final String subject,
		                                 final Map<String, String> payload,
		                                 final int expirationTimeMinutesInTheFuture) throws JoseException {


			return SymmetricJWT.generateJWT_AES128(
					key,
					issuer,
					audience,
					subject,
					payload,
					expirationTimeMinutesInTheFuture
			);
		}




		/**
		 * Generates a JWT token using AES_128_CBC_HMAC_SHA_256.
		 *
		 * @param key                              Valid cypher key for encryption
		 * @param issuer                           Corporate Name of the Issuer of this token.
		 * @param audience                         The audience of the token. That is whom it is meant for. Usually a corporate name.
		 * @param subject                          The subject of the token. Meaning what you are securing.
		 * @param payload                          The map of claims in JWT speak
		 * @param expirationTimeMinutesInTheFuture The maximum number of minutes this generated token is valid.
		 * @return JWT token string of the form string.string.string
		 * @throws JoseException Tossed if there is any failure during generation.
		 */
		public static String generateJWT_AES128(final SecretKey key,
		                                        final String issuer,
		                                        final String audience,
		                                        final String subject,
		                                        final Map<String, String> payload,
		                                        final int expirationTimeMinutesInTheFuture) throws JoseException {

			final JwtClaims claims = new JwtClaims();
			claims.setIssuer(issuer);  // who creates the token and signs it
			claims.setAudience(audience); // to whom the token is intended to be sent
			claims.setExpirationTimeMinutesInTheFuture(expirationTimeMinutesInTheFuture); // time when the token will expire (10 minutes from now)
			claims.setGeneratedJwtId(); // a unique identifier for the token
			claims.setIssuedAtToNow();  // when the token was issued/created (now)
			claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
			claims.setSubject(subject); // the subject/principal is whom the token is about


			payload.keySet().forEach(k -> claims.setStringClaim(k, payload.get(k)));

			final JsonWebEncryption jwe = new JsonWebEncryption();
			jwe.setPayload(claims.toJson());
			// Set the "alg" header, which indicates the key management mode for this JWE.
			// In this example we are using the direct key management mode, which means
			// the given key will be used directly as the content encryption key.
			jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);

			// Set the "enc" header, which indicates the content encryption algorithm to be used.
			// This example is using AES_128_CBC_HMAC_SHA_256 which is a composition of AES CBC
			// and HMAC SHA2 that provides authenticated encryption.
			jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
			jwe.setKey(key);

			String jwt = null;
			try {
				jwt = jwe.getCompactSerialization();
			} catch (JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}

			return jwt;
		}


	}

	//------------------------------- SymmetricJWT.class end -------------------------------


	//------------------------------- JsonWebKeyUtil.class start -------------------------------
	public static class JsonWebKeyUtil {

//	public final static Provider provider = new BouncyCastleProvider();

		// ---------------------------------- Json RSA Web Key from org.jose4j.jwk.  ----------------------------------

		public static RsaJsonWebKey produceSecureRsaWebKey() throws JoseException {


			try {

				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
				keyPairGenerator.initialize(2048);
				KeyPair kp = keyPairGenerator.generateKeyPair();
				RSAPublicKey pub = (RSAPublicKey) kp.getPublic();
				RSAPrivateKey priv = (RSAPrivateKey) kp.getPrivate();
				RsaJsonWebKey rsaJwk = (RsaJsonWebKey) newPublicJwk(pub);
				rsaJwk.setPrivateKey(priv);
				rsaJwk.setKeyId(UUID.randomUUID().toString());
				rsaJwk.setAlgorithm("RSA");
//		sign_jwk.setUse("sig");
				rsaJwk.setKeyId("123452");
				return rsaJwk;
			} catch (NoSuchAlgorithmException | JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}


		}

		public static RsaJsonWebKey generateRsaWebKey() throws JoseException {

			RsaJsonWebKey rsaJsonWebKey = null;
			try {
				rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
			} catch (JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}
			// Give the JWK a Key ID (kid), which is just the polite thing to do
			assert rsaJsonWebKey != null;
			rsaJsonWebKey.setKeyId(UUID.randomUUID().toString());
			rsaJsonWebKey.setAlgorithm("RSA");

			rsaJsonWebKey.setKeyId("123452");
			return rsaJsonWebKey;

		}


		public static RsaJsonWebKey getWebKeyFromKeyPair(KeyPair keyPair) throws JoseException {
			RsaJsonWebKey rsaJwk = null;
			try {
				rsaJwk = (RsaJsonWebKey) newPublicJwk(keyPair.getPublic());
			} catch (JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}
			assert rsaJwk != null;
			rsaJwk.setPrivateKey(keyPair.getPrivate());
			rsaJwk.setAlgorithm("RSA");
//		sign_jwk.setUse("sig");
			rsaJwk.setKeyId("123452");
			return rsaJwk;
		}

		/**
		 * @param rsaJsonWebKey org.jose4j.jwk.JsonWebKey
		 * @return KeyPair
		 * @throws NoSuchAlgorithmException newer
		 */
		public static KeyPair generateKeyPairFromJsonWebKey(RsaJsonWebKey rsaJsonWebKey)  {
			return new KeyPair(rsaJsonWebKey.getPublicKey(), rsaJsonWebKey.getRsaPrivateKey());
		}

		public static RsaJsonWebKey getRsaJsonWebKeyFromPublicAndPrivateKey(PublicKey publicKey, PrivateKey privateKey) throws JoseException {
			RsaJsonWebKey rsaJwk = null;
			try {
				rsaJwk = (RsaJsonWebKey) newPublicJwk(publicKey);
				rsaJwk.setPrivateKey(privateKey);
				rsaJwk.setAlgorithm("RSA");
				// sign_jwk.setUse("sig");
				rsaJwk.setKeyId("123452");
				return rsaJwk;
			} catch (JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}
		}

		public static RsaJsonWebKey getRsaJsonWebKeyFromPublic(PublicKey publicKey)  {
			RsaJsonWebKey sign_jwk = new RsaJsonWebKey((RSAPublicKey) publicKey);
			sign_jwk.setAlgorithm("RSA");
//		sign_jwk.setUse("sig");
			sign_jwk.setKeyId("123452");
			return sign_jwk;

		}

		public static String getPublicRsaWebKeyAsJson(RsaJsonWebKey rsaJwk) {
			return rsaJwk.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);
		}

		public static String getPrivateRsaWebKeyAsJson(RsaJsonWebKey rsaJwk) {
			return rsaJwk.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);
		}

		public static String getSymmetricRsaWebKeyAsJson(RsaJsonWebKey rsaJwk) {
			return rsaJwk.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);
		}


		public static RsaJsonWebKey getPublicRSAJwkFromJson(String publicRsaJsonJwk) throws JoseException {
			// parse and convert into PublicJsonWebKey object
			RsaJsonWebKey rsaJsonWebKey = (RsaJsonWebKey) newPublicJwk(publicRsaJsonJwk);
			rsaJsonWebKey.setAlgorithm("RSA");
//		sign_jwk.setUse("sig");
			rsaJsonWebKey.setKeyId("123452");

			return rsaJsonWebKey;
		}


		public static RsaJsonWebKey getPrivateRsaJwkFromJson(String PrivateRsaJsonJwk) throws JoseException {
			// parse and convert into JsonWebKey object
			RsaJsonWebKey rsaJsonWebKey = (RsaJsonWebKey) newPublicJwk(PrivateRsaJsonJwk);
//		rsaJsonWebKey.setAlgorithm("RSA");
//		sign_jwk.setUse("sig");
			rsaJsonWebKey.setKeyId("123452");

			return rsaJsonWebKey;
		}

		public static String convertPrivateRsaJwtToPublicRsaJwt(String PrivateRsaJsonJwk) throws JoseException {

			return getPublicRsaWebKeyAsJson((RsaJsonWebKey) newPublicJwk(PrivateRsaJsonJwk));

		}


		public static EllipticCurveJsonWebKey generateEllipticWebKey() throws JoseException {


			try {
				// Generate an EC key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
				EllipticCurveJsonWebKey Jwk = EcJwkGenerator.generateJwk(EllipticCurves.P256);

				// Give the JWK a Key ID (kid), which is just the polite thing to do
				Jwk.setKeyId(UUID.randomUUID().toString());

				return Jwk;
			} catch (JoseException e) {
				System.err.println("\nJoseException - " + e.toString() + "\n");
				throw new JoseException(e.getMessage(), e.getCause());
			}

		}


		public static String getPublicEcllipticWebKeyAsJson(EllipticCurveJsonWebKey rsaJwk) {
			return rsaJwk.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);
		}

		public static String getPrivateEcllipticWebKeyAsJson(EllipticCurveJsonWebKey asJWK) {
			return asJWK.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);
		}

		public static String getSymmetricEcllipticWebKeyAsJson(EllipticCurveJsonWebKey rsaJwk) {
			return rsaJwk.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);
		}

		public static EllipticCurveJsonWebKey getEllipticJwkFromJson(String ellipticJsonWebKey) throws JoseException {
			EllipticCurveJsonWebKey curvedJsonWebKey = null;
			try {
				curvedJsonWebKey = (EllipticCurveJsonWebKey) newPublicJwk(ellipticJsonWebKey);
			} catch (JoseException e) {
				e.printStackTrace();
				throw new JoseException(e.getMessage(), e.getCause());
			}

			if (curvedJsonWebKey.getKeyId() == null || curvedJsonWebKey.getKeyId().isEmpty()) {
				curvedJsonWebKey.setKeyId(UUID.randomUUID().toString());
			}

			curvedJsonWebKey.setAlgorithm("EC");

			return curvedJsonWebKey;
		}

		//------------------------------- JsonWebKeyUtil.SymmetricKey.class Start -------------------------------
		public static class SymmetricKey {

			public static SecretKey generateSecretAesKey()  {
//				Key key = new AesKey(ByteUtil.randomBytes(16));
//				SecretKeySpec secretKeySpec = new SecretKeySpec(ByteUtil.randomBytes(16), "AES");
//
//				SecretKey key2 = ;
				return new SecretKeySpec(ByteUtil.randomBytes(16), "AES");
			}

			public static SecretKey secretKeyToString(final String key) {
				return new SecretKeySpec(key.getBytes(), "AES");

			}

			public static SecretKey stringToSecretKey(final String key) {
				return new AesKey(Base64.decode(key));

			}


		}

		//------------------------------- JsonWebKeyUtil.SymmetricKey.class End -------------------------------


	}

	//------------------------------- JsonWebKeyUtil.class end -------------------------------


	//------------------------------- default.functions Start -------------------------------
	public static String ProduceJWT(
			String issue,
			String audience,
			String subject,
			String jsonPayload) throws JoseException {


		// The outer JWT is a JWE
		JsonWebSignature jws = new JsonWebSignature();

		// Create the Claims, which will be the content of the JWT
		JwtClaims claimAsPayload = produceClaim(issue, audience, subject, jsonPayload);
		// A nested JWT requires that the cty (Content Type) header be set to "JWT" in the outer JWT
		jws.setContentTypeHeaderValue("JWT");

		jws.setPayload(claimAsPayload.toJson());

		try {
			return jws.getCompactSerialization();
		} catch (JoseException e) {
			e.printStackTrace();
			throw new JoseException(e.getMessage(), e.getCause());
		}

	}

	public static String getJwtPayload(
			String issue,
			String audience,
			String subject,
			String token) throws JoseException {


		JwtConsumer jwtConsumer = new JwtConsumerBuilder()
				.setRequireExpirationTime() // the JWT must have an expiration time
				.setRequireSubject() // the JWT must have a subject claim
				.setExpectedIssuer(issue) // whom the JWT needs to have been issued by
				.setExpectedAudience(audience) // to whom the JWT is intended for
				.setExpectedSubject(subject)
				.build(); // create the JwtConsumer instance

		try {
			jwtConsumer.processToClaims(token);
		} catch (InvalidJwtException e) {
			e.printStackTrace();
		}


		try {
			return (String) jwtConsumer.processToClaims(token).getClaimValue("payload");
		} catch (InvalidJwtException e) {
			e.printStackTrace();
			throw new JoseException(e.getMessage(), e.getCause());
		}
	}


	public static JwtClaims getJwtClaimVerifyAndDecrypt(
			String issue,
			String audience,
			String senderPrivateEllipticJwt,
			String receiverPublicEllipticJwt,
			String token) throws JoseException {


		try {
			EllipticCurveJsonWebKey senderPublicJWK = JsonWebKeyUtil.getEllipticJwkFromJson(senderPrivateEllipticJwt);
			EllipticCurveJsonWebKey receiverPrivateJWK = JsonWebKeyUtil.getEllipticJwkFromJson(receiverPublicEllipticJwt);



		// Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
		// be used to validate and process the JWT.
		// The specific validation requirements for a JWT are context dependent, however,
		// it typically advisable to require a (reasonable) expiration time, a trusted issuer, and
		// and audience that identifies your system as the intended recipient.
		// If the JWT is encrypted too, you need only provide a decryption key or
		// decryption key resolver to the builder.
		JwtConsumer jwtConsumer = new JwtConsumerBuilder()
				.setRequireExpirationTime() // the JWT must have an expiration time
				.setRequireSubject() // the JWT must have a subject claim
				.setExpectedIssuer(issue) // whom the JWT needs to have been issued by
				.setExpectedAudience(audience) // to whom the JWT is intended for
				.setDecryptionKey(receiverPrivateJWK.getPrivateKey()) // decrypt wit receivers private key
				.setVerificationKey(senderPublicJWK.getPublicKey()) // verify the signature with the public key
				.build(); // create the JwtConsumer instance


			return jwtConsumer.processToClaims(token);
		} catch (InvalidJwtException e) {
			e.printStackTrace();
			throw new JoseException(e.getMessage(), e.getCause());
		}
	}


	public static JwtClaims getJwtClaimDecrypt(
			String issue,
			String audience,
			String receiverPublicEllipticJwt,
			String token) throws JoseException {

		try {
			EllipticCurveJsonWebKey receiverPrivateJWK = JsonWebKeyUtil.getEllipticJwkFromJson(receiverPublicEllipticJwt);

			JwtConsumer jwtConsumer = new JwtConsumerBuilder()
					.setRequireExpirationTime() // the JWT must have an expiration time
					.setRequireSubject() // the JWT must have a subject claim
					.setExpectedIssuer(issue) // whom the JWT needs to have been issued by
					.setExpectedAudience(audience) // to whom the JWT is intended for
					.setDecryptionKey(receiverPrivateJWK.getPrivateKey()) // decrypt wit receivers private key
					.build(); // create the JwtConsumer instance

			return jwtConsumer.processToClaims(token);

		} catch (InvalidJwtException | JoseException e) {
			e.printStackTrace();
			throw new JoseException(e.getMessage(), e.getCause());
		}


	}

	public static JwtClaims produceClaim(String issue, String audience, String subject, String jsonPayload) {
		// Create the Claims, which will be the content of the JWT
		JwtClaims claims = new JwtClaims();
		claims.setIssuer(issue);  // who creates the token and signs it
		claims.setAudience(audience); // to whom the token is intended to be sent
		claims.setSubject(subject); // the subject/principal is whom the token is about
		claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
		claims.setGeneratedJwtId(); // a unique identifier for the token
		claims.setIssuedAtToNow();  // when the token was issued/created (now)
		claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
		// multi-valued claims work too and will end up as a JSON array
/*		List<String> groups = Arrays.asList("user", "other-group", "group-three");
		claims.setStringListClaim("groups", groups); */
		claims.setClaim("payload", jsonPayload); // additional claims/attributes about the subject can be added
		return claims;
	}


	//------------------------------- default.functions End -------------------------------

}
