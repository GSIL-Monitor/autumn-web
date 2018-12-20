package org.autumn.commons.web.request;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.autumn.commons.Tracker;
import org.autumn.commons.Utils;
import org.autumn.commons.web.request.matcher.RequestMatchers;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : Autumn跟踪器过滤器，添加跟踪器ID和请求信息（URL，客户端IP等）<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnTrackerFilter extends OncePerRequestFilter {

    private static final RequestMatchers DEFAULT_REQUEST_MATCHERS = RequestMatchers.getInstance("/actuator/**,/*/actuator/**");

    private RequestMatchers requestMatchers = DEFAULT_REQUEST_MATCHERS;

    public void setProperties(AutumnRequestProperties properties) {
        if (null != properties && null != properties.getTracker()) {
            RequestMatchers rm = RequestMatchers.getInstance(properties.getTracker().getIgnorePatterns());
            this.requestMatchers = this.requestMatchers.merge(rm);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (!Tracker.isTracking()) {
                String trackId = request.getHeader(AutumnRequests.AUTUMN_TRACKER_HEADER_NAME);
                if (Utils.isBlank(trackId)) {
                    Tracker.start();
                } else {
                    Tracker.start(trackId);
                }

                String requestInfo = request.getHeader(AutumnRequests.AUTUMN_REQUEST_HEADER_NAME);
                if (Utils.isBlank(requestInfo)) {
                    requestInfo = buildRequestInfo(request);
                }
                Tracker.setTrackProperty(AutumnRequests.AUTUMN_REQUEST_HEADER_NAME, requestInfo, true);
            }
            filterChain.doFilter(request, response);
        } catch (Throwable t) {
            request.getRequestDispatcher("").forward(request, response);
        } finally {
            Tracker.stop();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return null != this.requestMatchers && this.requestMatchers.matches(request);
    }

    protected String buildRequestInfo(HttpServletRequest request) {
        return AutumnRequests.buildRequestInfo(request);
    }

}
