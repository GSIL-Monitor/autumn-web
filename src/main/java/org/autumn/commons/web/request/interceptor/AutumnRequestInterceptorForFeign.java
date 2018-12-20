package org.autumn.commons.web.request.interceptor;

import org.autumn.commons.Tracker;
import org.autumn.commons.Utils;
import org.autumn.commons.web.request.AutumnRequests;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : Feign请求拦截器，传递跟踪ID和请求信息<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnRequestInterceptorForFeign implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String trackId = Tracker.getTrackId();
        if (!Utils.isBlank(trackId)) {
            template.header(AutumnRequests.AUTUMN_TRACKER_HEADER_NAME, trackId);
        }
        String requestInfo = Tracker.getTrackProperty(AutumnRequests.AUTUMN_REQUEST_HEADER_NAME);
        if (!Utils.isBlank(requestInfo)) {
            template.header(AutumnRequests.AUTUMN_REQUEST_HEADER_NAME, requestInfo);
        }
    }
}
