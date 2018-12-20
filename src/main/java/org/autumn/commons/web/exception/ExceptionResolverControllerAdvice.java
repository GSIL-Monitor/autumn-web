package org.autumn.commons.web.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 控制器的异常处理切面，处理所有控制器执行过程中发生的异常，但不能处理其它过程中的异常<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@RestController
@ControllerAdvice
public class ExceptionResolverControllerAdvice {

    /**
     * 处理所有控制器抛出的异常
     *
     * @param t
     *
     * @return
     */
    @ExceptionHandler
    public Throwable resolveException(Throwable t) {
        return t;
    }
}
