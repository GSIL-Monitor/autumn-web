package org.autumn.commons.web.exception;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 异常处理的请求和处理器的映射器<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class ExceptionHandlerMapping extends AbstractHandlerMapping {

    /**
     * 异常信息存储在请求中的属性名称
     */
    private static final String EXCEPTION_IN_REQUEST_ATTR_NAME = ExceptionHandlerMapping.class.getName() + ".EXCEPTION_IN_REQUEST_ATTR_NAME";

    private static Method method;

    static {
        try {
            method = ExceptionHandlerMapping.class.getDeclaredMethod("doHandlerException", HttpServletRequest.class);
        } catch (Exception e) {
        }
    }

    /**
     * 构造函数，设置高优先级
     */
    public ExceptionHandlerMapping() {
        super();
        super.setOrder(HIGHEST_PRECEDENCE);
    }

    /**
     * 将异常设置到请求属性中
     *
     * @param request
     * @param exception
     */
    public static void setExceptionToRequestContext(HttpServletRequest request, Throwable exception) {
        request.setAttribute(EXCEPTION_IN_REQUEST_ATTR_NAME, exception);
    }

    /**
     * 从请求属性中获取异常
     *
     * @param request
     *
     * @return
     */
    public static Throwable getExceptionFromRequestContext(HttpServletRequest request) {
        Object exception = request.getAttribute(EXCEPTION_IN_REQUEST_ATTR_NAME);
        if (exception instanceof Throwable) {
            return (Throwable) exception;
        }
        return null;
    }

    /**
     * 处理异常，直接返回异常，然后在序列化的时候统一包装，而不是抛出异常
     *
     * @param request
     *
     * @return
     *
     * @throws Throwable
     */
    @ResponseBody
    public Throwable doHandlerException(HttpServletRequest request) throws Throwable {
        return (Throwable) request.getAttribute(EXCEPTION_IN_REQUEST_ATTR_NAME);
    }

    /**
     * 获取处理器
     */
    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        Object exception = request.getAttribute(EXCEPTION_IN_REQUEST_ATTR_NAME);
        if (exception instanceof Throwable) {
            return new HandlerMethod(this, method);
        }
        return null;
    }
}
