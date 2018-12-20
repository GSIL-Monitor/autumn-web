package org.autumn.commons.web.request.matcher;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.autumn.commons.Utils;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 请求匹配器<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class RequestMatchers {

    private final OrRequestMatcher matcher;

    /**
     * 私有的静态构造函数
     *
     * @param matcher
     */
    private RequestMatchers(OrRequestMatcher matcher) {
        this.matcher = matcher;
    }

    /**
     * 私有的静态构造函数，通过静态方法暴露出去
     *
     * @param patterns
     */
    private RequestMatchers(String patterns) {
        List<AntPathRequestMatcher> matchers = new ArrayList<>();
        for (String pattern : patterns.split("\\s*,\\s*")) {
            if (!Utils.isBlank(pattern)) {
                matchers.add(new AntPathRequestMatcher(pattern));
            }
        }
        if (!matchers.isEmpty()) {
            this.matcher = new OrRequestMatcher(matchers);
        } else {
            this.matcher = null;
        }
    }

    /**
     * 根据URL模式获取请求匹配器，URL模式不需要包括context-path
     *
     * @param patterns
     *
     * @return
     */
    public static RequestMatchers getInstance(String patterns) {
        if (Utils.isBlank(patterns)) {
            return null;
        }
        return new RequestMatchers(patterns);
    }

    /**
     * 合并匹配请求，只要满足其中之一即可
     *
     * @param requestMatchers
     *
     * @return
     */
    public RequestMatchers merge(RequestMatchers... requestMatchers) {
        List<AntPathRequestMatcher> matchers = new ArrayList<>();
        matchers.addAll(this.matcher.getRequestMatchers());
        for (RequestMatchers matcher : requestMatchers) {
            if (null != matcher && null != matcher.matcher) {
                List<AntPathRequestMatcher> aprs = matcher.matcher.getRequestMatchers();
                if (null != aprs) {
                    matchers.addAll(aprs);
                }
            }
        }
        if (!matchers.isEmpty()) {
            return new RequestMatchers(new OrRequestMatcher(matchers));
        } else {
            return null;
        }
    }

    /**
     * 判断请求是否匹配
     *
     * @param request
     *
     * @return
     */
    public boolean matches(HttpServletRequest request) {
        return null != matcher && matcher.matches(request);
    }
}
