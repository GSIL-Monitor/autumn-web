package org.autumn.commons.web.bind;

import org.autumn.commons.web.bind.provider.PropertyValuesProviders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 数据绑定自动配置<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnBindConfiguration {

    @Bean
    @ConditionalOnMissingBean(PropertyValuesProviders.class)
    public PropertyValuesProviders propertyValuesProviders() {
        return new PropertyValuesProviders();
    }

    @Bean
    @ConditionalOnMissingBean(AutumnBindWebMvcRegistrations.class)
    public AutumnBindWebMvcRegistrations autumnBindWebMvcRegistrations() {
        return new AutumnBindWebMvcRegistrations();
    }

    static class AutumnBindWebMvcRegistrations implements WebMvcRegistrations {

        @Override
        public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
            return new RelaxedRequestMappingHandlerAdapter();
        }
    }
}
