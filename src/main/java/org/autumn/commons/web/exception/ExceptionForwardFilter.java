package org.autumn.commons.web.exception;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.autumn.commons.exception.Throws;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 异常处理过滤器<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class ExceptionForwardFilter extends OncePerRequestFilter {

    private String errorUrl;

    private String app;

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public void setApp(String app) {
        this.app = app;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //如果已经是错误请求，就不再转发，已防止循环错误
        if ((request.getContextPath() + errorUrl).equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
        } else {
            HttpServletResponse resp = new AutumnHttpServletResponse(request, response, app, errorUrl);
            filterChain.doFilter(request, resp);
        }
    }

    /**
     * 拦截SendError，设置异常类，并将错误处理统一发送到${server.error.path:${error.path:/error}}
     */
    private static class AutumnHttpServletResponse extends HttpServletResponseWrapper {

        private final HttpServletRequest request;

        private final HttpServletResponse response;

        private final String app;

        private final String errorUrl;

        public AutumnHttpServletResponse(HttpServletRequest request, HttpServletResponse response, String app, String errorUrl) {
            super(response);
            this.request = request;
            this.response = response;
            this.app = app;
            this.errorUrl = errorUrl;
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            forwardError(sc, msg);
        }

        @Override
        public void sendError(int sc) throws IOException {
            forwardError(sc, null);
        }

        private void forwardError(int sc, String msg) throws IOException {
            try {
                Throwable throwable = ExceptionHandlerMapping.getExceptionFromRequestContext(request);
                if (null == throwable) {
                    // 将HTTP返回码添加000转换为Autumn的返回码
                    throwable = Throws.createException("000" + sc, app, request.getRequestURI());
                    ExceptionHandlerMapping.setExceptionToRequestContext(request, throwable);
                }
                request.getRequestDispatcher(request.getContextPath() + errorUrl).forward(request, response);
            } catch (Exception ignore) {
                super.sendError(sc, msg);
            }
        }
    }
}
