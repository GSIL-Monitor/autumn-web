package org.autumn.commons.web.swagger.configure;

import springfox.documentation.spring.web.plugins.Docket;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : Swagger配置器接口<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public interface SwaggerDocketConfigurer {

    void configure(Docket docket);
}
