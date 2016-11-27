package se.kth.awesome.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "awesome.security.key")
public class AwesomeServerKeys {

	private String sharedSecretKey;

	private String serverRsaPrivateKey;

	private String serverRsaPublicKey;

	private String encryptPayloadKey;

	public String getSharedSecretKey() {
		return sharedSecretKey;
	}
	public void setSharedSecretKey(String sharedSecretKey) {
		this.sharedSecretKey = sharedSecretKey;
	}

	public String getServerRsaPrivateKey() {
		return serverRsaPrivateKey;
	}
	public void setServerRsaPrivateKey(String serverRsaPrivateKey) {
		this.serverRsaPrivateKey = serverRsaPrivateKey;
	}

	public String getServerRsaPublicKey() {
		return serverRsaPublicKey;
	}
	public void setServerRsaPublicKey(String serverRsaPublicKey) {
		this.serverRsaPublicKey = serverRsaPublicKey;
	}


	public String getEncryptPayloadKey() {
		return encryptPayloadKey;
	}
	public void setEncryptPayloadKey(String encryptPayloadKey) {
		this.encryptPayloadKey = encryptPayloadKey;
	}
}
