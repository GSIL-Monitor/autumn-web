package org.autumn.commons.web.swagger.configure.impl;

import java.util.ArrayList;
import java.util.List;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 配置公共的请求参数<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class GlobalParametersConfigurer extends AbstractSwaggerDocketConfigurer {

    @Override
    public void configure(Docket docket) {
        List<Parameter> parameters = new ArrayList<>();

        Parameter consumerId = buildParameter(1, "consumerId", "String", "{{autumn.parameters.consumerId}}");
        parameters.add(consumerId);

        Parameter timestamp = buildParameter(2, "timestamp", "Long", "{{autumn.parameters.timestamp}}:System.currentTimeMillis()");
        parameters.add(timestamp);

        String signDesc = "</br>url: request.getRequestURI()</br>context: IOUtils.toString(request.getInputStream(),\"UTF-8\")";
        Parameter sign = buildParameter(3, "sign", "String", "{{autumn.parameters.sign}}:DigestUtils.md5Hex(consumerId+token+timestamp+url+context).toLowerCase()" + signDesc);
        parameters.add(sign);

        docket.globalOperationParameters(parameters);
    }

    private Parameter buildParameter(int order, String name, String type, String description) {
        return new ParameterBuilder()
                .name(name)
                .required(true)
                .parameterType("query") // header, cookie, body, query
                .modelRef(new ModelRef(type))
                .description(description)
                .order(order)
                .build();
    }
}
