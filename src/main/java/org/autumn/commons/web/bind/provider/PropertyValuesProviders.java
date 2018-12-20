package org.autumn.commons.web.bind.provider;

import java.util.List;
import javax.servlet.ServletRequest;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 属性值绑定的帮助类<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class PropertyValuesProviders {

    private static List<IPropertyValuesProvider> providers;

    @Autowired(required = false)
    public void setProviders(List<IPropertyValuesProvider> providers) {
        if (providers != null) {
            PropertyValuesProviders.providers = providers;
        }
    }

    public static void addBindValues(MutablePropertyValues mpvs, ServletRequest request, Object target, String name) {
        if (null != providers) {
            for (IPropertyValuesProvider provider : providers) {
                provider.addBindValues(mpvs, request, target, name);
            }
        }
    }

    public static void afterBindValues(PropertyAccessor accessor, ServletRequest request, Object target, String name) {
        if (null != providers) {
            for (IPropertyValuesProvider provider : providers) {
                provider.afterBindValues(accessor, request, target, name);
            }
        }
    }
}
