package se.kth.awesome.security.auth.jwt;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import se.kth.awesome.SpringbootSecurityJwtApplication;

import static se.kth.awesome.util.Util.nLin;

/**
 * SkipPathRequestMatcher
 * 
 * @author vladimir.stankovic
 *
 * Aug 19, 2016
 */
public class SkipPathRequestMatcher implements RequestMatcher {
    private OrRequestMatcher matchers;
    private RequestMatcher processingMatcher;
    private final Logger logger2 = LoggerFactory.getLogger(getClass());
    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
        Assert.notNull(pathsToSkip);
        List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        logger2.error(nLin+nLin+""+ SpringbootSecurityJwtApplication.steps++ +" ---------- SkipPathRequestMatcher.matches ----------\n");

        if (matchers.matches(request)) {
            return false;
        }
        return processingMatcher.matches(request) ? true : false;
    }
}
