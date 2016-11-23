package se.kth.awesome.security.auth.jwt.verifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.kth.awesome.SpringbootSecurityJwtApplication;

import static se.kth.awesome.util.Util.nLin;

/**
 * BloomFilterTokenVerifier
 * 
 * @author vladimir.stankovic
 *
 * Aug 17, 2016
 */

//TODO verify token
@Component
public class BloomFilterTokenVerifier implements TokenVerifier {
    private final Logger logger2 = LoggerFactory.getLogger(getClass());
    @Override
    public boolean verify(String jti) {
        logger2.error(nLin+nLin+""+ SpringbootSecurityJwtApplication.steps++ +" ---------- BloomFilterTokenVerifier.verify ----------\n");

        return true;
    }
}
