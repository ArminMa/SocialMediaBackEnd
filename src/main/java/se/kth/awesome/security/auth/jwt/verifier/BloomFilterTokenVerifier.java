package se.kth.awesome.security.auth.jwt.verifier;

import org.springframework.stereotype.Component;

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
    @Override
    public boolean verify(String jti) {
        return true;
    }
}
