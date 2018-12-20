package org.autumn.commons.web.response.code;

import java.util.EnumSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.autumn.commons.spring.SpringBootHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 响应码帮助类，自动搜索所有实现了{@link ResponseCode}的实现类<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public abstract class ResponseCodeHolder {

    // 按键值的自然顺序排序
    private static final Map<String, String> codes = new TreeMap<>();

    private static final AtomicBoolean monitor = new AtomicBoolean();

    private ResponseCodeHolder() {
    }

    /**
     * 获取所有响应码（默认扫描SpringBoot应用对应的BasePackage包，如果想扫描其它包，可以先调用{@link #scanPackage(String)}）
     *
     * @return
     */
    public static Map<String, String> getCodes() {
        if (!monitor.get()) {
            synchronized (monitor) {
                if (!monitor.get()) {
                    for (String basePackage : SpringBootHolder.getBasePackages()) {
                        scanningResponseCodes(codes, basePackage);
                    }
                    monitor.set(true);
                }
            }
        }
        return codes;
    }

    /**
     * 扫描basePackage包，并合并扫描结果
     *
     * @param basePackage
     */
    public static void scanPackage(String basePackage) {
        Map<String, String> codes = new TreeMap<>();
        scanningResponseCodes(codes, basePackage);
        synchronized (ResponseCodeHolder.codes) {
            ResponseCodeHolder.codes.putAll(codes);
        }
    }

    private static void scanningResponseCodes(Map<String, String> codes, String basePackage) {
        try {
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            String base = (basePackage + "/**/*").replaceAll("[.]", "/");
            Resource[] resources = resolver.getResources("classpath*:" + base + ".class");
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        ClassMetadata metadata = metadataReader.getClassMetadata();
                        if (metadata.isAbstract() || metadata.isInterface()) {
                            continue;
                        } else {
                            Class<?> cls = ClassUtils.forName(metadata.getClassName(), null);
                            if (!ResponseCode.class.isAssignableFrom(cls)) {
                                continue;
                            }
                            // 如果是枚举，则获取所有枚举的实例
                            if (cls.isEnum()) {
                                for (Object e : getAllEnum(cls)) {
                                    putResponseCode(codes, (ResponseCode) e);
                                }
                            } else {
                                putResponseCode(codes, (ResponseCode) cls.newInstance());
                            }
                        }
                    } catch (Throwable ex) {
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    private static void putResponseCode(Map<String, String> codes, ResponseCode responseCode) {
        String code = responseCode.getCode();
        if (null != code && !codes.containsKey(code)) {
            codes.put(code, responseCode.getDesc());
        }
    }

    private static EnumSet getAllEnum(Class cls) {
        return EnumSet.allOf(cls);
    }

}
