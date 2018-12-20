package org.autumn.commons.web.response;

import org.autumn.commons.web.response.feign.AutumnResponseFeignDecoder;
import org.autumn.commons.web.response.wrapper.exception.BindExceptionResultWrapper;
import org.autumn.commons.web.response.wrapper.page.PageResultWrapper;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 响应自动配置<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnResponseConfiguration {

    @Autowired(required = false)
    private SpringDataWebProperties springDataWebProperties;

    /**
     * 分页包装器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PageResultWrapper.class)
    public PageResultWrapper pageResultWrapper() {
        PageResultWrapper wrapper = new PageResultWrapper();
        wrapper.setSpringDataWebProperties(springDataWebProperties);
        return wrapper;
    }

    /**
     * 数据绑定异常包装器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(BindExceptionResultWrapper.class)
    public BindExceptionResultWrapper bindExceptionResultWrapper() {
        BindExceptionResultWrapper wrapper = new BindExceptionResultWrapper();
        return wrapper;
    }

    /**
     * 控制器返回结果序列化JSON前的切面
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AutumnResponseBodyAdvice.class)
    public AutumnResponseBodyAdvice autumnResponseBodyAdvice() {
        AutumnResponseBodyAdvice advice = new AutumnResponseBodyAdvice();
        return advice;
    }

    /**
     * Feign RPC调用的响应处理
     */
    @Configuration
    @ConditionalOnClass({Decoder.class, SpringDecoder.class})
    static class DecoderConfiguration {

        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Decoder autumnResponseDecoder() {
            return new OptionalDecoder(new AutumnResponseFeignDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters))));
        }
    }
}
