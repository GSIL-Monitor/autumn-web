package org.autumn.commons.web.response.code;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : Autumn的响应码（全部以00开头）<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public enum ResponseCodes implements ResponseCode {

    _000000("000000", "请求成功"),
    _000200("000200", "请求成功"),
    _009999("009999", "请求失败"),
    _000404("000404", "未找到服务"),
    _000500("000500", "服务器错误"),
    _009401("009401", "缺少参数：appId"),
    _009402("009402", "缺少参数：请求签名(sign)"),
    _009403("009403", "缺少参数：请求时间戳(timestamp)"),
    _009404("009404", "请求时间戳必须为整数"),
    _009405("009405", "请求签名已过期"),
    _009406("009406", "appId未注册，或者未生成签名Token"),
    _009407("009407", "签名错误"),
    _009408("009408", "没有权限"),
    _009409("009409", "Token已过期");

    private String code;

    private String desc;

    ResponseCodes(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
