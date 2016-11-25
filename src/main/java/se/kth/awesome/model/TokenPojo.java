package se.kth.awesome.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import se.kth.awesome.util.gsonX.GsonX;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL )
public class TokenPojo implements Serializable,Comparable<TokenPojo>{

	private String token ;
	private String refreshToken;

	public TokenPojo() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TokenPojo tokenPojo = (TokenPojo) o;

		if (token != null ? !token.equals(tokenPojo.token) : tokenPojo.token != null) return false;
		return refreshToken != null ? refreshToken.equals(tokenPojo.refreshToken) : tokenPojo.refreshToken == null;

	}

	@Override
	public int hashCode() {
		int result = token != null ? token.hashCode() : 0;
		result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(TokenPojo o) {
		int thisObject= this.hashCode();
		long anotherObject = o.hashCode();
		return (thisObject<anotherObject ? -1 : (thisObject==anotherObject ? 0 : 1));
	}

	@Override
	public String toString() {
		return  GsonX.gson.toJson(this);
	}
}
