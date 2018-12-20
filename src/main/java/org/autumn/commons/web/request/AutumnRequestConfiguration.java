package org.autumn.commons.web.request;

import javax.servlet.DispatcherType;

import org.autumn.commons.web.request.interceptor.AutumnRequestInterceptorForFeign;
import org.autumn.commons.web.request.interceptor.AutumnRequestInterceptorForRestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import feign.RequestInterceptor;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 请求自动配置<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@EnableConfigurationProperties(AutumnRequestProperties.class)
public class AutumnRequestConfiguration {

    private final AutumnRequestProperties properties;

    public AutumnRequestConfiguration(AutumnRequestProperties properties) {
        this.properties = properties;
    }

    /**
     * 跟踪器过滤器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AutumnTrackerFilter.class)
    public FilterRegistrationBean<AutumnTrackerFilter> autumnTrackerFilter() {
        FilterRegistrationBean<AutumnTrackerFilter> filter = new FilterRegistrationBean<>();
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE + 100);

        String[] urlPatterns = null;
        if (null != properties && null != properties.getTracker() && null != properties.getTracker().getPatterns()) {
            urlPatterns = properties.getTracker().getPatterns().split("\\s*,\\s*");
        } else {
            urlPatterns = new String[] {"/*"};
        }
        filter.addUrlPatterns(urlPatterns);
        filter.setDispatcherTypes(DispatcherType.REQUEST, new DispatcherType[] {DispatcherType.FORWARD});
        filter.setAsyncSupported(true);

        AutumnTrackerFilter autumnTrackerFilter = new AutumnTrackerFilter();
        autumnTrackerFilter.setProperties(properties);
        filter.setFilter(autumnTrackerFilter);
        return filter;
    }

    /**
     * Feign请求拦截器
     */
    @Configuration
    @ConditionalOnClass({RequestInterceptor.class})
    @ConditionalOnMissingBean(AutumnRequestInterceptorForFeign.class)
    static class RequestInterceptorForFeignConfiguration {

        @Bean
        public RequestInterceptor autumnRequestInterceptorForFeign() {
            return new AutumnRequestInterceptorForFeign();
        }
    }


    /**
     * RestTemplate请求拦截器
     */
    @Configuration
    @ConditionalOnBean({RestTemplate.class})
    static class RequestInterceptorForRestTemplateConfiguration {

        @Bean
        @ConditionalOnMissingBean(AutumnRequestInterceptorForRestTemplate.class)
        public ClientHttpRequestInterceptor autumnRequestInterceptorForRestTemplate() {
            return new AutumnRequestInterceptorForRestTemplate();
        }

        @Bean
        @ConditionalOnMissingBean(AutumnRestTemplateInterceptorCustomizer.class)
        public AutumnRestTemplateInterceptorCustomizer autumnRestTemplateInterceptorCustomizer(AutumnRequestInterceptorForRestTemplate interceptor) {
            AutumnRestTemplateInterceptorCustomizer customizer = new AutumnRestTemplateInterceptorCustomizer(interceptor);
            return customizer;
        }

        private static class AutumnRestTemplateInterceptorCustomizer implements RestTemplateCustomizer {

            private AutumnRequestInterceptorForRestTemplate interceptor;

            public AutumnRestTemplateInterceptorCustomizer(AutumnRequestInterceptorForRestTemplate interceptor) {
                this.interceptor = interceptor;
            }

            @Override
            public void customize(RestTemplate restTemplate) {
                restTemplate.getInterceptors().add(interceptor);
            }
        }
    }
}
