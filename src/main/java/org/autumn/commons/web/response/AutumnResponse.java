package org.autumn.commons.web.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : Autumn的统一响应类<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@Getter
@Setter
public class AutumnResponse<D> implements Serializable {

    private static final long serialVersionUID = 8770946342336554288L;

    public static final String ERROR_CODE = "error-code";

    /**
     * 返回代码，使用双引号作为国际化的标志，{@link #ERROR_CODE}表示需要使用响应码替换
     */
    @ApiModelProperty(value = "{{autumn.response.code}}{{@" + ERROR_CODE + "}}", position = 1, required = true, dataType = "String")
    private String code;

    /**
     * 返回描述
     */
    @ApiModelProperty(value = "{{autumn.response.message}}", position = 2, example = "{{autumn.response.message.example}}", required = true, dataType = "String")
    private String message;

    /**
     * 是否成功
     */
    @ApiModelProperty(value = "{{autumn.response.success}}", position = 3, allowableValues = "true, false", required = true, dataType = "Boolean")
    private boolean success;

    /**
     * 跟踪ID
     */
    @ApiModelProperty(value = "{{autumn.response.trackId}}", position = 4, required = true, dataType = "String")
    private String trackId;

    /**
     * 响应数据
     */
    @ApiModelProperty(value = "{{autumn.response.data}}", position = 5, allowEmptyValue = true)
    private D data;
}
