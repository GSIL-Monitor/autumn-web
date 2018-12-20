package org.autumn.commons.web.swagger.plugins;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.autumn.commons.Utils;
import org.autumn.commons.spring.SpringHolder;
import org.autumn.commons.web.response.AutumnResponse;
import org.autumn.commons.web.response.code.ResponseCodeHolder;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 重定义JsonSerializer，实现国际化功能<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnJsonSerializer extends JsonSerializer {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(.*?)(?=}})\\}\\}");

    private static final Pattern NUM_PATTERN = Pattern.compile("\\{.*?\\d+.*?\\}");

    private final Cache<String, String> cache;

    public AutumnJsonSerializer(List<JacksonModuleRegistrar> modules) {
        super(modules);
        this.cache = CacheBuilder.newBuilder().maximumSize(1000).build();
    }

    @Override
    public Json toJson(Object toSerialize) {
        Json json = super.toJson(toSerialize);
        if (null != json && null != json.value()) {
            String value = transform(json.value());
            return new Json(value);
        }
        return json;
    }

    private String transform(String src) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(src);
        while (matcher.find()) {
            String replacement = cachedResolveReplacement(matcher.group(1), matcher.group(0));
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String cachedResolveReplacement(String placeholder, String src) {
        String value = cache.getIfPresent(placeholder);
        if (null == value) {
            synchronized (cache) {
                value = cache.getIfPresent(placeholder);
                if (null == value) {
                    value = resolveReplacement(placeholder, src);
                    cache.put(placeholder, value);
                }
            }
        }
        return value;
    }

    protected String resolveReplacement(String placeholder, String src) {
        if (placeholder.startsWith("@")) {
            String name = placeholder.substring(1);
            String value = this.resolveFunctionReplacement(name);
            return Utils.isBlank(value) ? src : value;
        } else {
            return getLocaleMessage(placeholder, src);
        }
    }

    protected String resolveFunctionReplacement(String name) {
        if (AutumnResponse.ERROR_CODE.equals(name)) {
            Map<String, String> codes = ResponseCodeHolder.getCodes();
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : codes.entrySet()) {
                String code = entry.getKey();
                String desc = getLocaleMessage(code, entry.getValue());
                sb.append("</br>").append(code).append(":").append(desc);
            }
            return sb.toString();
        } else {
            String[] arr = name.split("[:]");
            String fname = arr[0];
            if ("date".equalsIgnoreCase(fname)) {
                String format = "yyyyMMdd";
                if (arr.length >= 2) {
                    format = arr[1];
                }
                return new SimpleDateFormat(format).format(new Date());
            }
        }
        return null;
    }

    private String getLocaleMessage(String code, String defaultValue) {
        try {
            String rs = SpringHolder.getMessage(code, null, defaultValue);
            rs = NUM_PATTERN.matcher(rs).replaceAll("");
            return rs;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
