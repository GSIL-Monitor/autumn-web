package org.autumn.commons.web.swagger.expander.impl;

import java.util.ArrayList;
import java.util.List;

import org.autumn.commons.Utils;
import org.autumn.commons.web.swagger.expander.SwaggerParameterExpander;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spring.web.readers.parameter.ExpansionContext;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 分页请求参数<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-19<br>
 */
public class PageableParameterExpander implements SwaggerParameterExpander {

    private SpringDataWebProperties springDataWebProperties;

    public void setSpringDataWebProperties(SpringDataWebProperties springDataWebProperties) {
        this.springDataWebProperties = springDataWebProperties;
    }

    @Override
    public boolean supports(ExpansionContext context) {
        return context.getParamType().isInstanceOf(Pageable.class);
    }

    @Override
    public List<Parameter> expand(ExpansionContext context) {
        return pageParameters(springDataWebProperties);
    }

    private List<Parameter> pageParameters(SpringDataWebProperties properties) {
        List<Parameter> parameters = new ArrayList<>();

        SpringDataWebProperties.Pageable pageable = properties.getPageable();
        String prefix = pageable.getPrefix();
        if (Utils.isBlank(prefix)) {//没有前缀
            prefix = "";
        }

        String pageName = prefix + pageable.getPageParameter();
        Parameter page = buildParameter(1, pageName, "int", "1", "{{autumn.parameters.page.page}}");
        parameters.add(page);

        String sizeName = prefix + pageable.getSizeParameter();
        Parameter size = buildParameter(2, sizeName, "int", "20", "{{autumn.parameters.page.size}}");
        parameters.add(size);

        // 排序参数
        String sortName = properties.getSort().getSortParameter();
        Parameter sort = buildParameter(3, sortName, "String", null, "{{autumn.parameters.sort}}");
        parameters.add(sort);

        return parameters;
    }

    private Parameter buildParameter(int order, String name, String type, String defaultValue, String description) {
        return new ParameterBuilder()
                .name(name)
                .required(false)
                .parameterType("query") // header, cookie, body, query
                .modelRef(new ModelRef(type))
                .description(description)
                .order(order)
                .defaultValue(defaultValue)
                .build();
    }
}
