package org.autumn.commons.web.request.interceptor;

import java.io.IOException;

import org.autumn.commons.Tracker;
import org.autumn.commons.Utils;
import org.autumn.commons.web.request.AutumnRequests;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : RestTemplate请求拦截器，传递跟踪ID和请求信息<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnRequestInterceptorForRestTemplate implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        String trackId = Tracker.getTrackId();
        if (!Utils.isBlank(trackId)) {
            headers.add(AutumnRequests.AUTUMN_TRACKER_HEADER_NAME, trackId);
        }

        String requestInfo = Tracker.getTrackProperty(AutumnRequests.AUTUMN_REQUEST_HEADER_NAME);
        if (!Utils.isBlank(requestInfo)) {
            headers.add(AutumnRequests.AUTUMN_REQUEST_HEADER_NAME, requestInfo);
        }
        return execution.execute(request, body);
    }
}
