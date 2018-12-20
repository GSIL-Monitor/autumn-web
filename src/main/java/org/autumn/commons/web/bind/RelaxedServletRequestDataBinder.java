package org.autumn.commons.web.bind;

import javax.servlet.ServletRequest;

import org.autumn.commons.web.bind.provider.PropertyValuesProviders;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.AbstractPropertyBindingResult;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 重定义数据绑定工厂器，包权限，通过{@link PropertyValuesProviders}提供数据绑定过程中切入点：绑定前、绑定后的自定义操作<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
/* package */ class RelaxedServletRequestDataBinder extends ExtendedServletRequestDataBinder {

    public RelaxedServletRequestDataBinder(Object target) {
        super(target);
    }

    public RelaxedServletRequestDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    /**
     * 添加属性值提供器的相关处理
     */
    @Override
    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        super.addBindValues(mpvs, request);
        PropertyValuesProviders.addBindValues(mpvs, request, getTarget(), getObjectName());
    }

    /**
     * 常规绑定之后的处理
     */
    @Override
    public void bind(ServletRequest request) {
        super.bind(request);
        PropertyValuesProviders.afterBindValues(getPropertyAccessor(), request, getTarget(), getObjectName());
    }

    @Override
    protected AbstractPropertyBindingResult createBeanPropertyBindingResult() {
        return new RelaxedBeanPropertyBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths(), getAutoGrowCollectionLimit());
    }
}
