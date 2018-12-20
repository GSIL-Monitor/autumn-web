package org.autumn.commons.web.response.feign;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.autumn.commons.exception.Throws;
import org.autumn.commons.web.response.AutumnResponse;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : Feign RPC响应处理<br>
 * <p>如果Feign接口方法的定义中返回值不是AutumnResponse，则先序列化为AutumnResponse，再转换为接口方法中定义的返回类型，从而自适配Feign中的返回类型
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnResponseFeignDecoder implements Decoder {

    private Decoder decoder;

    public AutumnResponseFeignDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        // 如果Feign接口中已经是AutumnResponse类型，直接返回
        if (isParameterizeAutumnResponseType(type)) {
            return decoder.decode(response, type);
        } else {
            // 否则先使用AutumnResponse反序列化响应值，然后再转换
            Type newType = ParameterizedTypeImpl.make(AutumnResponse.class, new Type[] {type}, null);
            AutumnResponse<Object> rs = (AutumnResponse<Object>) decoder.decode(response, newType);
            if (null == rs) {
                return null;
            } else if (rs.isSuccess()) {
                return rs.getData();
            } else {
                throw Throws.createCodeAndMessageException(rs.getCode(), rs.getMessage());
            }
        }
    }

    private boolean isParameterizeAutumnResponseType(Type type) {
        if (type instanceof ParameterizedType) {
            return isAutumnResponseType(((ParameterizedType) type).getRawType());
        }
        return false;
    }

    private boolean isAutumnResponseType(Type type) {
        if (type instanceof Class) {
            Class c = (Class) type;
            return AutumnResponse.class.isAssignableFrom(c);
        }
        return false;
    }
}
