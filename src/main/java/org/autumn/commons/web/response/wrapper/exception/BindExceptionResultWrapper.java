package org.autumn.commons.web.response.wrapper.exception;

import org.autumn.commons.web.response.AutumnResponse;
import org.autumn.commons.web.response.wrapper.ResultWrapper;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 绑定异常包装器<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class BindExceptionResultWrapper implements ResultWrapper {

    @Override
    public boolean supports(Object result, MethodParameter returnType) {
        return result instanceof BindException;
    }

    @Override
    public void wrap(AutumnResponse<Object> response, Object result, MethodParameter returnType) {
        BindException exception = (BindException) result;
        response.setMessage(getMessage(exception));
    }

    private String getMessage(BindException exception) {
        BindingResult result = exception.getBindingResult();
        StringBuilder msg = new StringBuilder();
        if (result.hasFieldErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                msg.append(",").append(error.getDefaultMessage());
            }
        }
        if (result.hasGlobalErrors()) {
            for (ObjectError error : result.getGlobalErrors()) {
                msg.append(",").append(error.getDefaultMessage());
            }
        }
        return msg.substring(1);
    }
}
