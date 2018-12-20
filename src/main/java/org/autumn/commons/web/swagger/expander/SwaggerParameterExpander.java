package org.autumn.commons.web.swagger.expander;

import java.util.List;

import springfox.documentation.service.Parameter;
import springfox.documentation.spring.web.readers.parameter.ExpansionContext;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 扩展请求参数<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-19<br>
 */
public interface SwaggerParameterExpander {

    /**
     * 是否适配
     *
     * @param context
     *
     * @return
     */
    boolean supports(ExpansionContext context);

    /**
     * 扩展请求参数
     *
     * @param context
     *
     * @return
     */
    List<Parameter> expand(ExpansionContext context);
}
