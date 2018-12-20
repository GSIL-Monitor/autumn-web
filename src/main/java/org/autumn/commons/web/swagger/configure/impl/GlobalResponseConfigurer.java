package org.autumn.commons.web.swagger.configure.impl;

import springfox.documentation.spring.web.plugins.Docket;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 配置响应属性<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class GlobalResponseConfigurer extends AbstractSwaggerDocketConfigurer {

    @Override
    public void configure(Docket docket) {
        // 不生成默认的404等返回码
        docket.useDefaultResponseMessages(false);
    }
}
