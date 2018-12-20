package org.autumn.commons.web.response.wrapper.page;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 分页响应<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-19<br>
 */
@Getter
@Setter
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = -8284716565770784912L;

    @ApiModelProperty(value = "{{autumn.response.page.total}}", position = 1, example = "885", required = true, dataType = "long")
    private long total;

    @ApiModelProperty(value = "{{autumn.response.page.size}}", position = 2, example = "20", required = true, dataType = "int")
    private int size;

    @ApiModelProperty(value = "{{autumn.response.page.page}}", position = 3, example = "1", required = true, dataType = "int")
    private int page;

    @ApiModelProperty(value = "{{autumn.response.page.first}}", position = 4, example = "true", allowableValues = "true, false", required = true, dataType = "Boolean")
    private boolean first;

    @ApiModelProperty(value = "{{autumn.response.page.last}}", position = 5, example = "false", allowableValues = "true, false", required = true, dataType = "Boolean")
    private boolean last;

    @ApiModelProperty(value = "{{autumn.response.page.list}}", position = 6, allowEmptyValue = true, dataType = "list")
    private List<T> list;
}
