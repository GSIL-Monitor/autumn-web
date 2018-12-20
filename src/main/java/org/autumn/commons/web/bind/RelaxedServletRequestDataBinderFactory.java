package org.autumn.commons.web.bind;

import java.util.List;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 重定义数据绑定工厂，包权限<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
/* package */ class RelaxedServletRequestDataBinderFactory extends ServletRequestDataBinderFactory {

    public RelaxedServletRequestDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer) {
        super(binderMethods, initializer);
    }

    @Override
    protected ServletRequestDataBinder createBinderInstance(Object target, String objectName, NativeWebRequest request) {
        return new RelaxedServletRequestDataBinder(target, objectName);
    }
}
