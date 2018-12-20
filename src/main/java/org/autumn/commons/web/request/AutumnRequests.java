package org.autumn.commons.web.request;

import javax.servlet.http.HttpServletRequest;

import org.autumn.commons.EnvConsts;
import org.autumn.commons.Tracker;
import org.autumn.commons.Utils;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 请求帮助类<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-14<br>
 */
public class AutumnRequests {
    /**
     * 表示请求信息的头部
     */
    public static final String AUTUMN_REQUEST_HEADER_NAME = "AUTUMN_REQUEST";
    /**
     * 表示当前跟踪ID的头部
     */
    public static final String AUTUMN_TRACKER_HEADER_NAME = "AUTUMN_TRACKER_HEADER_NAME";
    /**
     * 表示消费者的参数名称
     */
    public static final String AUTUMN_CONSUMER_PARAM_NAME = "consumerId";

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

    /**
     * 构建请求信息
     *
     * @param request
     *
     * @return
     */
    public static String buildRequestInfo(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String consumerId = request.getParameter(AUTUMN_CONSUMER_PARAM_NAME);
        if (!Utils.isBlank(consumerId)) {
            sb.append("[CONSUMER-ID:").append(consumerId).append("]");
        }
        sb.append("[TRACK-ID:").append(Tracker.getTrackId()).append("]");
        sb.append("[").append(request.getMethod()).append(" ").append(request.getRequestURI()).append("]");
        sb.append("[CLIENT-IP:").append(getClientIp(request)).append("]");
        return sb.toString();
    }

    /**
     * 获取客户端IP
     *
     * @param request
     *
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = getRemoteIpAddress(request);
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "0.0.0.0".equals(ip) || "localhost".equals(ip)) {
            ip = EnvConsts.IP;
        }
        return ip;
    }

    private static String getRemoteIpAddress(HttpServletRequest request) {
        for (String header : AutumnRequests.HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (!Utils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                if (-1 != ip.indexOf(",")) {
                    ip = ip.split(",")[0];
                    return ip;
                }
            }
        }
        return request.getRemoteAddr();
    }
}
