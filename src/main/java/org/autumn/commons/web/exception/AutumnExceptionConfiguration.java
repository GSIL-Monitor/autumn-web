package org.autumn.commons.web.exception;

import javax.servlet.DispatcherType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 异常自动配置<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnExceptionConfiguration {

    @Value("${server.error.path:${error.path:/error}}")
    private String errorUrl;

    @Value("${spring.application.name:'autumn'}")
    private String applicationName;

    @Bean
    @ConditionalOnMissingBean(ExceptionResolverControllerAdvice.class)
    public ExceptionResolverControllerAdvice exceptionResolverControllerAdvice() {
        ExceptionResolverControllerAdvice exceptionResolverControllerAdvice = new ExceptionResolverControllerAdvice();
        return exceptionResolverControllerAdvice;
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionHandlerMapping.class)
    public ExceptionHandlerMapping exceptionHandlerMapping() {
        ExceptionHandlerMapping exceptionHandlerMapping = new ExceptionHandlerMapping();
        return exceptionHandlerMapping;
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionForwardFilter.class)
    public FilterRegistrationBean<ExceptionForwardFilter> autumnExceptionForwardFilter() {
        FilterRegistrationBean<ExceptionForwardFilter> filter = new FilterRegistrationBean<>();
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE + 200);//在AutumnTrackFilter之后

        filter.addUrlPatterns(new String[] {"/*"});
        filter.setDispatcherTypes(DispatcherType.REQUEST, new DispatcherType[] {DispatcherType.FORWARD});
        filter.setAsyncSupported(true);

        ExceptionForwardFilter exceptionForwardFilter = new ExceptionForwardFilter();
        exceptionForwardFilter.setApp(applicationName);
        exceptionForwardFilter.setErrorUrl(errorUrl);
        filter.setFilter(exceptionForwardFilter);
        return filter;
    }
}
