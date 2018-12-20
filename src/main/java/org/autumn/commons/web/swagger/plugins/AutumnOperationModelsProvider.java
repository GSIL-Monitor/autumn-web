package org.autumn.commons.web.swagger.plugins;

import java.util.List;

import org.autumn.commons.web.response.AutumnResponse;
import org.autumn.commons.web.response.wrapper.page.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.readers.operation.OperationModelsProvider;

import static springfox.documentation.schema.ResolvedTypes.resolvedTypeSignature;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 重定义OperationModelsProvider，实现AutumnResponse相关处理<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnOperationModelsProvider extends OperationModelsProvider {

    private static final Logger LOG = LoggerFactory.getLogger(AutumnOperationModelsProvider.class);
    private final TypeResolver typeResolver;

    public AutumnOperationModelsProvider(TypeResolver typeResolver) {
        super(typeResolver);
        this.typeResolver = typeResolver;
    }

    @Override
    public void apply(RequestMappingContext context) {
        collectFromReturnType(context);
        collectParameters(context);
        collectGlobalModels(context);
    }

    private void collectGlobalModels(RequestMappingContext context) {
        for (ResolvedType each : context.getAdditionalModels()) {
            context.operationModelsBuilder().addInputParam(each);
            context.operationModelsBuilder().addReturn(each);
        }
    }

    private void collectFromReturnType(RequestMappingContext context) {
        ResolvedType resolvedType = context.getReturnType();
        if (!resolvedType.isInstanceOf(AutumnResponse.class)) {
            if (resolvedType.isInstanceOf(Page.class)) {
                // 分页
                resolvedType = typeResolver.resolve(resolvedType.getTypeBindings(), PageResponse.class);
            }
            resolvedType = typeResolver.resolve(AutumnResponse.class, resolvedType);
        }
        ResolvedType modelType = context.alternateFor(resolvedType);
        modelType = context.alternateFor(modelType);
        LOG.debug("Adding return parameter of type {}", resolvedTypeSignature(modelType).or("<null>"));
        context.operationModelsBuilder().addReturn(modelType);
    }

    private void collectParameters(RequestMappingContext context) {

        LOG.debug("Reading parameters models for handlerMethod |{}|", context.getName());

        List<ResolvedMethodParameter> parameterTypes = context.getParameters();
        for (ResolvedMethodParameter parameterType : parameterTypes) {
            if (parameterType.hasParameterAnnotation(RequestBody.class)
                    || parameterType.hasParameterAnnotation(RequestPart.class)) {
                ResolvedType modelType = context.alternateFor(parameterType.getParameterType());
                LOG.debug("Adding input parameter of type {}", resolvedTypeSignature(modelType).or("<null>"));
                context.operationModelsBuilder().addInputParam(modelType);
            }
        }
        LOG.debug("Finished reading parameters models for handlerMethod |{}|", context.getName());
    }
}
