package sbs.apidemo.user.api.controller;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeBindings;
import com.fasterxml.classmate.types.ResolvedObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import sbs.apidemo.base.argumentresolver.dtotovo.DtoToVo;
import sbs.apidemo.base.argumentresolver.votodto.VoToDto;
import springfox.documentation.schema.plugins.SchemaPluginsManager;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ViewProviderPlugin;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static springfox.documentation.schema.ResolvedTypes.resolvedTypeSignature;


@Order(Ordered.HIGHEST_PRECEDENCE)
public class OperationModelsProviderTest implements OperationModelsProviderPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(springfox.documentation.spring.web.readers.operation.OperationModelsProvider.class);
    private final SchemaPluginsManager pluginsManager;

    @Autowired
    public OperationModelsProviderTest(SchemaPluginsManager pluginsManager) {
        this.pluginsManager = pluginsManager;
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

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private void collectFromReturnType(RequestMappingContext context) {

        ResolvedType modelType = null;

        if (context.findAnnotation(DtoToVo.class).isPresent()) {
            DtoToVo dtoToVo = context.findAnnotation(DtoToVo.class).get();

            Class<?> vo = dtoToVo.vo();
            HttpStatus status = dtoToVo.status();


            ResolvedObjectType resolvedTypeVo = new ResolvedObjectType(
                    vo,
                    context.getReturnType().getTypeBindings(),
                    context.getReturnType().findSupertype(Object.class),
                    context.getReturnType().getImplementedInterfaces()
            );

            modelType = resolvedTypeVo;
        } else {
            modelType = context.getReturnType();
        }

        modelType = context.alternateFor(modelType);
        LOG.debug(
                "Adding return parameter of type {}",
                resolvedTypeSignature(modelType).orElse("<null>"));

        context.operationModelsBuilder()
                .addReturn(
                        modelType,
                        viewForReturn(context));
    }

    private void collectParameters(RequestMappingContext context) {
        LOG.debug(
                "Reading parameters models for handlerMethod |{}|",
                context.getName());

        List<ResolvedMethodParameter> parameterTypes = context.getParameters();
        for (ResolvedMethodParameter parameterType : parameterTypes) {
            if (parameterType.hasParameterAnnotation(VoToDto.class)) {

                /**
                 * VoToDto 클래스 안에 있는 vo 값 가져오기
                 */
                Class<?> vo = parameterType.findAnnotation(VoToDto.class).get().vo();
                String voName = vo.getSimpleName();

                /**
                 * class -> ResolvedObjectType 으로 변경
                 */
                ResolvedObjectType resolvedTypeVo = new ResolvedObjectType(
                        vo,
                        parameterType.getParameterType().getTypeBindings(),
                        parameterType.getParameterType().findSupertype(Object.class),
                        parameterType.getParameterType().getImplementedInterfaces()
                );

                ResolvedMethodParameter resolvedMethodParameterVo
                        = new ResolvedMethodParameter(0, voName, parameterType.getAnnotations(), resolvedTypeVo);


                ResolvedType modelType = context.alternateFor(resolvedTypeVo);

                LOG.debug(
                        "Adding input parameter of type {}",
                        resolvedTypeSignature(modelType).orElse("<null>"));
                context.operationModelsBuilder().addInputParam(
                        modelType,
                        viewForParameter(
                                context,
                                resolvedMethodParameterVo),
                        new HashSet<>());
            }
        }
        LOG.debug(
                "Finished reading parameters models for handlerMethod |{}|",
                context.getName());
    }

    private Optional<ResolvedType> viewForReturn(RequestMappingContext context) {
        ViewProviderPlugin viewProvider =
                pluginsManager.viewProvider(context.getDocumentationContext().getDocumentationType());
        return viewProvider.viewFor(
                context);
    }

    private Optional<ResolvedType> viewForParameter(
            RequestMappingContext context,
            ResolvedMethodParameter parameter) {
        ViewProviderPlugin viewProvider =
                pluginsManager.viewProvider(context.getDocumentationContext().getDocumentationType());
        return viewProvider.viewFor(
                parameter);
    }
}
