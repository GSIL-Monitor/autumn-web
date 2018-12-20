package org.autumn.commons.web.request;

import org.autumn.commons.Consts;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 请求配置属性<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = AutumnRequestProperties.PREFIX)
public class AutumnRequestProperties {

    public static final String PREFIX = Consts.PROPERTIES_PREFIX + ".request";

    @NestedConfigurationProperty
    private TrackerProperties tracker;


    @Getter
    @Setter
    public static class TrackerProperties {

        /**
         * 过滤器中不需要添加trackId的URL模式
         */
        private String ignorePatterns;

        /**
         * 需要过滤的URL模式
         */
        private String patterns;
    }
}
