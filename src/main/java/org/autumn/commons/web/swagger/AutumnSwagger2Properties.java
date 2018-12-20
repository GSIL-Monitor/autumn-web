package org.autumn.commons.web.swagger;

import org.autumn.commons.Consts;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : Swagger的配置属性<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = AutumnSwagger2Properties.PREFIX)
public class AutumnSwagger2Properties {

    public static final String PREFIX = Consts.PROPERTIES_PREFIX + ".swagger2";

    /**
     * 组名
     */
    private String groupName;

    /**
     * 测试调用的主机
     */
    private String host;

    /**
     * 测试调用的上下文
     */
    private String contextPath;

    /**
     * API属性
     */
    @NestedConfigurationProperty
    private Api api;


    /**
     * @see springfox.documentation.service.ApiInfo
     */
    @Getter
    @Setter
    public static class Api {
        private String title;
        private String description;
        private String termsOfServiceUrl;
        @NestedConfigurationProperty
        private Contact contact;
        private String license;
        private String licenseUrl;
        private String version;
    }


    /**
     * @see springfox.documentation.service.Contact
     */
    @Getter
    @Setter
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }
}
