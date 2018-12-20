package org.autumn.commons.web.swagger.configure.impl;

import org.autumn.commons.web.swagger.AutumnSwagger2Properties;
import org.autumn.commons.web.swagger.configure.SwaggerDocketConfigurer;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 抽象的Swagger配置器<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public abstract class AbstractSwaggerDocketConfigurer implements SwaggerDocketConfigurer {

    protected AutumnSwagger2Properties properties;

    public void setProperties(AutumnSwagger2Properties properties) {
        this.properties = properties;
    }
}
