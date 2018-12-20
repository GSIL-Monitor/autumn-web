package org.autumn.commons.web.swagger.expander;

import java.util.List;

import springfox.documentation.schema.property.bean.AccessorsProvider;
import springfox.documentation.schema.property.field.FieldProvider;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spring.web.readers.parameter.ExpansionContext;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 重定义ModelAttributeParameterExpander，扩展请求参数的解析<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-19<br>
 */
public class AutumnModelAttributeParameterExpander extends ModelAttributeParameterExpander {

    private List<SwaggerParameterExpander> expanders;

    public void setExpanders(List<SwaggerParameterExpander> expanders) {
        this.expanders = expanders;
    }

    public AutumnModelAttributeParameterExpander(FieldProvider fields, AccessorsProvider accessors, EnumTypeDeterminer enumTypeDeterminer) {
        super(fields, accessors, enumTypeDeterminer);
    }

    @Override
    public List<Parameter> expand(ExpansionContext context) {
        if (null != expanders) {
            for (SwaggerParameterExpander expander : expanders) {
                if (expander.supports(context)) {
                    return expander.expand(context);
                }
            }
        }
        return super.expand(context);
    }
}
