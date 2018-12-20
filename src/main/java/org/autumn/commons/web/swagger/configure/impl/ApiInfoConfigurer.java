package org.autumn.commons.web.swagger.configure.impl;

import org.autumn.commons.Utils;
import org.autumn.commons.spring.SpringBootHolder;
import org.autumn.commons.web.swagger.AutumnSwagger2Properties.Api;
import org.autumn.commons.web.swagger.AutumnSwagger2Properties.Contact;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 根据属性配置API信息<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class ApiInfoConfigurer extends AbstractSwaggerDocketConfigurer {

    @Override
    public void configure(Docket docket) {
        docket.select()
                .apis(input -> declaringClass(input).transform(handlerPackage()).or(true))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(buildApiInfo());
    }

    private ApiInfo buildApiInfo() {
        Api api = properties.getApi();
        Assert.notNull(api, "the api config is null, please check...");
        ApiInfoBuilder builder = new ApiInfoBuilder()
                .title(api.getTitle())
                .description(api.getDescription())
                .termsOfServiceUrl(api.getTermsOfServiceUrl())
                .license(api.getLicense())
                .licenseUrl(api.getTermsOfServiceUrl())
                .version(getPropertyOrDefault(api.getVersion(), "2.0"));

        if (null != api.getContact()) {
            Contact c = api.getContact();
            builder.contact(new springfox.documentation.service.Contact(c.getName(), c.getUrl(), c.getEmail()));
        }
        return builder.build();
    }

    private String getPropertyOrDefault(String propertyValue, String defaultValue) {
        return Utils.isBlank(propertyValue) ? defaultValue : propertyValue;
    }

    private Function<Class<?>, Boolean> handlerPackage() {
        return input -> {
            String packageName = ClassUtils.getPackageName(input);
            for (String basePackage : SpringBootHolder.getBasePackages()) {
                if (packageName.startsWith(basePackage)) {
                    return true;
                }
            }
            return false;
        };
    }

    private Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
}
