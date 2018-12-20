package org.autumn.commons.web.swagger;

import java.util.List;
import javax.servlet.ServletContext;

import org.autumn.commons.web.swagger.configure.SwaggerDocketConfigurer;
import org.autumn.commons.web.swagger.configure.impl.AbstractSwaggerDocketConfigurer;
import org.autumn.commons.web.swagger.configure.impl.ApiInfoConfigurer;
import org.autumn.commons.web.swagger.configure.impl.GlobalParametersConfigurer;
import org.autumn.commons.web.swagger.configure.impl.GlobalResponseConfigurer;
import org.autumn.commons.web.swagger.configure.impl.HostAndPathConfigurer;
import org.autumn.commons.web.swagger.expander.AutumnModelAttributeParameterExpander;
import org.autumn.commons.web.swagger.expander.SwaggerParameterExpander;
import org.autumn.commons.web.swagger.expander.impl.PageableParameterExpander;
import org.autumn.commons.web.swagger.plugins.AutumnJsonSerializer;
import org.autumn.commons.web.swagger.plugins.AutumnOperationModelsProvider;
import org.autumn.commons.web.swagger.plugins.AutumnResponseMessagesReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.schema.property.bean.AccessorsProvider;
import springfox.documentation.schema.property.field.FieldProvider;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : Swagger自动配置<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@EnableConfigurationProperties(AutumnSwagger2Properties.class)
public class AutumnSwagger2Configuration {

    private final AutumnSwagger2Properties properties;

    private final SpringDataWebProperties springDataWebProperties;

    private final ServletContext servletContext;

    public AutumnSwagger2Configuration(
            AutumnSwagger2Properties properties,
            SpringDataWebProperties springDataWebProperties,
            ServletContext servletContext) {
        this.properties = properties;
        this.springDataWebProperties = springDataWebProperties;
        this.servletContext = servletContext;
    }

    /** ========插件============*/
    /**
     * 重定义ResponseMessagesReader，添加AutumnResponse的相关处理
     *
     * @param typeResolver
     * @param typeNameExtractor
     *
     * @return
     */
    @Primary
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public AutumnResponseMessagesReader responseMessagesReader(TypeResolver typeResolver, TypeNameExtractor typeNameExtractor) {
        return new AutumnResponseMessagesReader(typeResolver, typeNameExtractor);
    }

    /**
     * 重定义OperationModelsProvider，添加AutumnResponse的相关处理
     *
     * @param typeResolver
     *
     * @return
     */
    @Primary
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public AutumnOperationModelsProvider operationModelsProvider(TypeResolver typeResolver) {
        return new AutumnOperationModelsProvider(typeResolver);
    }

    /**
     * 重定义JsonSerializer，实现API文档国际化功能
     *
     * @param modules
     *
     * @return
     */
    @Primary
    @Bean
    public AutumnJsonSerializer jsonSerializer(List<JacksonModuleRegistrar> modules) {
        return new AutumnJsonSerializer(modules);
    }

    /**
     * ========扩展请求参数============
     */
    @Bean
    @ConditionalOnMissingBean(PageableParameterExpander.class)
    public PageableParameterExpander pageableParameterExpander() {
        PageableParameterExpander expander = new PageableParameterExpander();
        expander.setSpringDataWebProperties(springDataWebProperties);
        return expander;
    }

    /**
     * 重定义ModelAttributeParameterExpander，添加请求参数扩展的入口
     *
     * @param fields
     * @param accessors
     * @param enumTypeDeterminer
     *
     * @return
     */
    @Primary
    @Bean
    public AutumnModelAttributeParameterExpander autumnModelAttributeParameterExpander(
            FieldProvider fields, AccessorsProvider accessors, EnumTypeDeterminer enumTypeDeterminer,
            @Autowired(required = false) List<SwaggerParameterExpander> expanders) {
        AutumnModelAttributeParameterExpander expander = new AutumnModelAttributeParameterExpander(fields, accessors, enumTypeDeterminer);
        expander.setExpanders(expanders);
        return expander;
    }

    /** ========配置============*/
    /**
     * 配置主机和上下文
     *
     * @return
     */
    @Order(0)
    @Bean
    @ConditionalOnMissingBean(HostAndPathConfigurer.class)
    public HostAndPathConfigurer hostAndPathConfigurer() {
        HostAndPathConfigurer configurer = newSwaggerDocketConfigurer(HostAndPathConfigurer.class);
        configurer.setServletContext(servletContext);
        return configurer;
    }

    /**
     * 配置API信息
     *
     * @return
     */
    @Order(1)
    @Bean
    @ConditionalOnMissingBean(ApiInfoConfigurer.class)
    public ApiInfoConfigurer apiInfoConfigurer() {
        return newSwaggerDocketConfigurer(ApiInfoConfigurer.class);
    }

    /**
     * 配置全局请求参数
     *
     * @return
     */
    @Order(2)
    @Bean
    @ConditionalOnMissingBean(GlobalParametersConfigurer.class)
    public GlobalParametersConfigurer globalParametersConfigurer() {
        return newSwaggerDocketConfigurer(GlobalParametersConfigurer.class);
    }

    /**
     * 配置全局响应参数
     *
     * @return
     */
    @Order(3)
    @Bean
    @ConditionalOnMissingBean(GlobalResponseConfigurer.class)
    public GlobalResponseConfigurer globalResponseConfigurer() {
        return newSwaggerDocketConfigurer(GlobalResponseConfigurer.class);
    }

    /**
     * Swagger配置
     *
     * @param swaggerDocketConfigurers
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public Docket swaggerApi(@Autowired(required = false) List<SwaggerDocketConfigurer> swaggerDocketConfigurers) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        if (null != swaggerDocketConfigurers && !swaggerDocketConfigurers.isEmpty()) {
            for (SwaggerDocketConfigurer configurer : swaggerDocketConfigurers) {
                configurer.configure(docket);
            }
        }
        return docket;
    }

    private <E extends AbstractSwaggerDocketConfigurer> E newSwaggerDocketConfigurer(Class<E> cls) {
        try {
            E instance = cls.newInstance();
            instance.setProperties(properties);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
