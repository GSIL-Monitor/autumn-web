package org.autumn.commons.web.response.wrapper;

import org.autumn.commons.web.response.AutumnResponse;
import org.springframework.core.MethodParameter;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 结果包装器接口<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public interface ResultWrapper {

    /**
     * 是否适用
     *
     * @param returnType 返回类型
     *
     * @return
     */
    boolean supports(Object result, MethodParameter returnType);

    /**
     * 包装对象
     *
     * @param result     返回结果
     * @param returnType 返回类型
     */
    void wrap(AutumnResponse<Object> response, Object result, MethodParameter returnType);
}
