package org.autumn.commons.web.response.code;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 响应码接口<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public interface ResponseCode {

    /**
     * 响应码
     *
     * @return
     */
    String getCode();

    /**
     * 响应描述
     *
     * @return
     */
    String getDesc();
}
