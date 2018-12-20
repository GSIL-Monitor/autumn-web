package org.autumn.commons.web.response;

import java.lang.reflect.Method;
import java.util.List;

import org.autumn.commons.Tracker;
import org.autumn.commons.exception.Throws;
import org.autumn.commons.spring.SpringBootHolder;
import org.autumn.commons.spring.SpringHolder;
import org.autumn.commons.web.response.code.ResponseCodes;
import org.autumn.commons.web.response.wrapper.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 控制器切面，在序列化为JSON之前使用包装器统一包装<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@ControllerAdvice
public class AutumnResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired(required = false)
    private List<ResultWrapper> wrappers;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        // 所有异常
        if (Throwable.class.isAssignableFrom(method.getReturnType())) {
            return true;
        }
        // 指定包
        Class<?> beanType = method.getDeclaringClass();
        String packageName = beanType.getPackage().getName();
        for (String basePackage : SpringBootHolder.getBasePackages()) {
            if (packageName.startsWith(basePackage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return wrap(body, returnType);
    }

    private Object wrap(Object result, MethodParameter returnType) {
        if (result instanceof AutumnResponse) {
            return result;
        }

        AutumnResponse<Object> response = new AutumnResponse<>();
        response.setTrackId(Tracker.getTrackId());
        if (!(result instanceof Throwable)) {
            response.setSuccess(true);
            response.setCode(ResponseCodes._000000.getCode());
            response.setMessage(ResponseCodes._000000.getDesc());
        } else {
            response.setSuccess(false);
        }
        ResultWrapper wrapper = getResultWrapper(result, returnType);
        if (null == wrapper) {
            defaultWrap(response, result, returnType);
        } else {
            wrapper.wrap(response, result, returnType);
        }
        return response;
    }

    private ResultWrapper getResultWrapper(Object result, MethodParameter returnType) {
        if (null != result && null != wrappers) {
            for (ResultWrapper wrapper : wrappers) {
                if (wrapper.supports(result, returnType)) {
                    return wrapper;
                }
            }
        }
        return null;
    }

    private void defaultWrap(AutumnResponse<Object> response, Object result, MethodParameter returnType) {
        if (result instanceof Throwable) {
            Throwable t = (Throwable) result;
            String message = "[" + returnType.getMethod() + "]";
            Throwable root = Throws.getRootCause(t);
            if (null == root) {
                root = t;
            }
            message += "[cause: " + root.getClass().getName() + "]";
            message += t.getMessage();

            response.setCode(ResponseCodes._009999.getCode());
            response.setMessage(message);
            response.setData(null);
        } else {
            response.setCode(ResponseCodes._000000.getCode());
            response.setMessage(SpringHolder.getMessage(ResponseCodes._000000.getCode(), null));
            response.setData(result);
        }
    }
}
