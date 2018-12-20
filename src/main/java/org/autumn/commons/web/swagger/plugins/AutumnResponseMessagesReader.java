package org.autumn.commons.web.swagger.plugins;

import java.util.List;

import org.autumn.commons.web.response.AutumnResponse;
import org.autumn.commons.web.response.wrapper.page.PageResponse;
import org.springframework.data.domain.Page;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.readers.operation.ResponseMessagesReader;

import static com.google.common.collect.Sets.newHashSet;
import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 重定义ResponseMessagesReader，实现AutumnResponse相关处理<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class AutumnResponseMessagesReader extends ResponseMessagesReader {

    private final TypeResolver typeResolver;
    private final TypeNameExtractor typeNameExtractor;

    public AutumnResponseMessagesReader(TypeResolver typeResolver, TypeNameExtractor typeNameExtractor) {
        super(typeNameExtractor);
        this.typeNameExtractor = typeNameExtractor;
        this.typeResolver = typeResolver;
    }

    @Override
    public void apply(OperationContext context) {
        List<ResponseMessage> responseMessages = context.getGlobalResponseMessages(context.httpMethod().toString());
        context.operationBuilder().responseMessages(newHashSet(responseMessages));
        applyReturnTypeOverride(context);
    }

    private void applyReturnTypeOverride(OperationContext context) {
        ResolvedType resolvedType = context.getReturnType();
        if (!resolvedType.isInstanceOf(AutumnResponse.class)) {
            if (resolvedType.isInstanceOf(Page.class)) {
                // 分页
                resolvedType = typeResolver.resolve(resolvedType.getTypeBindings(), PageResponse.class);
            }
            resolvedType = typeResolver.resolve(AutumnResponse.class, resolvedType);
        }
        ResolvedType returnType = context.alternateFor(resolvedType);
        int httpStatusCode = httpStatusCode(context);
        String message = message(context);
        ModelContext modelContext = ModelContext.returnValue(
                context.getGroupName(),
                returnType,
                context.getDocumentationType(),
                context.getAlternateTypeProvider(),
                context.getGenericsNamingStrategy(),
                context.getIgnorableParameterTypes());
        ModelReference modelRef = modelRefFactory(modelContext, typeNameExtractor).apply(returnType);

        ResponseMessage built = new ResponseMessageBuilder()
                .code(httpStatusCode)
                .message(message)
                .responseModel(modelRef)
                .build();
        context.operationBuilder().responseMessages(newHashSet(built));
    }
}
