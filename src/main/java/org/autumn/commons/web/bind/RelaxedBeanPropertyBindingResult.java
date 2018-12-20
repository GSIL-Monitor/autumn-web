package org.autumn.commons.web.bind;

import org.springframework.beans.BeanWrapper;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 重定义数据绑定结果，包权限<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
/* package */ class RelaxedBeanPropertyBindingResult extends BeanPropertyBindingResult {
    private static final long serialVersionUID = -8321282528993868220L;

    public RelaxedBeanPropertyBindingResult(Object target, String objectName, boolean autoGrowNestedPaths, int autoGrowCollectionLimit) {
        super(target, objectName, autoGrowNestedPaths, autoGrowCollectionLimit);
    }

    @Override
    protected BeanWrapper createBeanWrapper() {
        BeanWrapper beanWrapper = new RelaxedBeanWrapper(getTarget());
        return beanWrapper;
    }
}
