package org.autumn.commons.web.bind.provider.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletRequest;

import org.autumn.commons.Logs;
import org.autumn.commons.crypto.KeyPairBean;
import org.autumn.commons.crypto.RSAUtils;
import org.autumn.commons.web.bind.annotation.RsaDecrypt;
import org.autumn.commons.web.bind.provider.IPropertyValuesProvider;
import org.springframework.beans.PropertyAccessor;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 抽象的RSA解密属性值提供器<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public abstract class AbstractRsaDecryptPropertyValuesProvider implements IPropertyValuesProvider {

    private Cache<Class<?>, List<Field>> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

    @Override
    public void afterBindValues(PropertyAccessor accessor, ServletRequest request, Object target, String name) {
        KeyPairBean pair = this.getRsaKeyPair(accessor, request, target, name);
        if (null == pair) {
            return;
        }
        for (Class<?> cls = target.getClass(); !cls.equals(Object.class); cls = cls.getSuperclass()) {
            List<Field> fields = resolveFields(cls);
            if (null != fields && !fields.isEmpty()) {
                for (Field field : fields) {
                    setDecryptValue(target, pair, field);
                }
            }
        }
    }

    abstract protected KeyPairBean getRsaKeyPair(PropertyAccessor accessor, ServletRequest request, Object target, String name);

    private void setDecryptValue(Object target, KeyPairBean pair, Field field) {
        try {
            Object value = field.get(target);
            if (value instanceof String) {
                String text = RSAUtils.decryptByPrivateKey((String) value, pair.getPrivateKey());
                field.set(target, text);
            }
        } catch (Exception e) {
            Logs.error("decrypt error: " + field, e);
        }
    }

    private List<Field> resolveFields(Class<?> cls) {
        List<Field> fieldList = cache.getIfPresent(cls);
        if (null == fieldList) {
            fieldList = new ArrayList<>();
            Field[] fields = cls.getDeclaredFields();
            if (null != fields) {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(RsaDecrypt.class)) {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        fieldList.add(field);
                    }
                }
            }
            if (fieldList.isEmpty()) {
                fieldList = Collections.emptyList();
            }
            cache.put(cls, fieldList);
        }
        return fieldList;
    }
}
