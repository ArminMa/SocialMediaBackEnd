package se.kth.awesome.security.auth.jwt.verifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.kth.awesome.SpringbootSecurityJwtApplication;
import se.kth.awesome.model.user.UserPojo;
import se.kth.awesome.security.auth.jwt.model.token.JwtSettings;
import se.kth.awesome.security.auth.jwt.model.token.JwtTokenFactory;
import se.kth.awesome.util.gsonX.GsonX;

import static se.kth.awesome.util.Util.nLin;

/**
 * BloomFilterTokenVerifier
 * 
 * @author vladimir.stankovic
 *
 * Aug 17, 2016
 */


@Component
public class BloomFilterTokenVerifier implements TokenVerifier {
    private final Logger logger2 = LoggerFactory.getLogger(getClass());
    @Autowired private JwtTokenFactory tokenFactory;

    public BloomFilterTokenVerifier() {}

    @Override
    public boolean verify(String jti) {
        logger2.error(nLin+nLin+""+ SpringbootSecurityJwtApplication.steps++ +" ---------- BloomFilterTokenVerifier.verify ----------"+nLin);
        if(tokenFactory.isValid(jti)){
           UserPojo userPojo = GsonX.gson.fromJson(tokenFactory.getPayloadFromJwt(jti), UserPojo.class);
           String userName = tokenFactory.getSubject(jti);
            if (userPojo.getUsername().equals(userName) ){
                return true;
            }

        }
        return false;
    }
}
