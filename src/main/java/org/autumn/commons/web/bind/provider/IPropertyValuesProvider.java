package org.autumn.commons.web.bind.provider;

import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessor;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 属性值提供器接口<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public interface IPropertyValuesProvider {

    /**
     * 绑定请添加属性
     *
     * @param mpvs
     * @param request
     * @param target
     * @param name
     */
    default void addBindValues(MutablePropertyValues mpvs, ServletRequest request, Object target, String name) {
    }

    /**
     * 绑定后修改目标对象
     *
     * @param accessor
     * @param request
     * @param target
     * @param name
     */
    default void afterBindValues(PropertyAccessor accessor, ServletRequest request, Object target, String name) {
    }
}
