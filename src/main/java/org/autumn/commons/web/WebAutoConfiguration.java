package org.autumn.commons.web;

import org.autumn.commons.web.bind.AutumnBindConfiguration;
import org.autumn.commons.web.exception.AutumnExceptionConfiguration;
import org.autumn.commons.web.request.AutumnRequestConfiguration;
import org.autumn.commons.web.response.AutumnResponseConfiguration;
import org.autumn.commons.web.validation.AutumnValidationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : Autumn Web组件自动配置<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@Configuration
@Import({
        AutumnBindConfiguration.class,
        AutumnValidationConfiguration.class,
        AutumnRequestConfiguration.class,
        AutumnResponseConfiguration.class,
        AutumnExceptionConfiguration.class
})
public class WebAutoConfiguration {

}
