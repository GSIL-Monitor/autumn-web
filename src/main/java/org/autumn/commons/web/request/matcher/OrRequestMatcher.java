package org.autumn.commons.web.request.matcher;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 为了没有引入spring-security-web的应用也可以使用，这里导入spring-security-web的两个类<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 *
 * @see org.springframework.security.web.util.matcher.OrRequestMatcher
 */
final class OrRequestMatcher {
    private final Log logger = LogFactory.getLog(getClass());
    private final List<AntPathRequestMatcher> requestMatchers;

    /**
     * Creates a new instance
     *
     * @param requestMatchers the {@link AntPathRequestMatcher} instances to try
     */
    public OrRequestMatcher(List<AntPathRequestMatcher> requestMatchers) {
        Assert.notEmpty(requestMatchers, "requestMatchers must contain a value");
        if (requestMatchers.contains(null)) {
            throw new IllegalArgumentException(
                    "requestMatchers cannot contain null values");
        }
        this.requestMatchers = requestMatchers;
    }

    /**
     * Creates a new instance
     *
     * @param requestMatchers the {@link AntPathRequestMatcher} instances to try
     */
    public OrRequestMatcher(AntPathRequestMatcher... requestMatchers) {
        this(Arrays.asList(requestMatchers));
    }

    public List<AntPathRequestMatcher> getRequestMatchers() {
        return requestMatchers;
    }

    public boolean matches(HttpServletRequest request) {
        for (AntPathRequestMatcher matcher : requestMatchers) {
            if (logger.isDebugEnabled()) {
                logger.debug("Trying to match using " + matcher);
            }
            if (matcher.matches(request)) {
                logger.debug("matched");
                return true;
            }
        }
        logger.debug("No matches found");
        return false;
    }

    @Override
    public String toString() {
        return "OrRequestMatcher [requestMatchers=" + requestMatchers + "]";
    }
}
