package sbs.apidemo.user.api.controller;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.types.ResolvedObjectType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sbs.apidemo.base.argumentresolver.votodto.VoToDto;
import springfox.documentation.common.Compatibility;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.ParameterAggregator;
import springfox.documentation.spring.web.readers.parameter.ExpansionContext;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.slf4j.LoggerFactory.getLogger;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class OperationBuilderPluginTest implements OperationBuilderPlugin {
    private static final Logger LOGGER = getLogger(OperationBuilderPluginTest.class);
    private final ModelAttributeParameterExpander expander;
    private final EnumTypeDeterminer enumTypeDeterminer;
    private final ParameterAggregator aggregator;

    @Autowired
    private DocumentationPluginsManager pluginsManager;

    @Autowired
    public OperationBuilderPluginTest(
            ModelAttributeParameterExpander expander,
            EnumTypeDeterminer enumTypeDeterminer,
            ParameterAggregator aggregator) {
        this.expander = expander;
        this.enumTypeDeterminer = enumTypeDeterminer;
        this.aggregator = aggregator;
    }

    @Override
    public void apply(OperationContext context) {

        context.operationBuilder().requestParameters(new HashSet<>(new HashSet<>()));

        List<Compatibility<springfox.documentation.service.Parameter, RequestParameter>> compatibilities
                = findVoToDto(context);

        /**
         * 2.0 버전
         *  - 나중에 버전 업데이트 확인해보기
         */
        context.operationBuilder().parameters(
                compatibilities.stream()
                        .map(Compatibility::getLegacy)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()));


        /**
         * 3.0 버전
         */
        context.operationBuilder().requestParameters(new HashSet<>(new HashSet<>()));
        Collection<RequestParameter> requestParameters = compatibilities.stream()
                .map(Compatibility::getModern)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toSet());
        context.operationBuilder()
                .requestParameters(aggregator.aggregate(requestParameters));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private List<Compatibility<springfox.documentation.service.Parameter, RequestParameter>>
    findVoToDto(OperationContext context) {

        List<Compatibility<springfox.documentation.service.Parameter, RequestParameter>> parameters = new ArrayList<>();
        List<ResolvedMethodParameter> methodParameters = context.getParameters();

        for (ResolvedMethodParameter methodParameter : methodParameters) {
            LOGGER.debug("Reading parameters for method {} at path {}", context.getName(), context.requestMappingPattern());
            if (methodParameter.hasParameterAnnotation(VoToDto.class)) {

                /**
                 * VoToDto 클래스 안에 있는 vo 값 가져오기
                 */
                Class<?> vo = methodParameter.findAnnotation(VoToDto.class).get().vo();
                String voName = vo.getSimpleName();

                /**
                 * class -> ResolvedObjectType 으로 변경
                 */
                ResolvedObjectType resolvedTypeVo = new ResolvedObjectType(
                        vo,
                        methodParameter.getParameterType().getTypeBindings(),
                        methodParameter.getParameterType().findSupertype(Object.class),
                        methodParameter.getParameterType().getImplementedInterfaces()
                );

                ResolvedMethodParameter resolvedMethodParameterVo
                        = new ResolvedMethodParameter(0, voName, methodParameter.getAnnotations(), resolvedTypeVo);

                LOGGER.debug("Processing parameter {}", methodParameter.defaultName().orElse("<unknown>"));
                ResolvedType alternate = context.alternateFor(resolvedTypeVo);

                ParameterContext parameterContext = new ParameterContext(methodParameter,
                        context.getDocumentationContext(),
                        context.getGenericsNamingStrategy(),
                        context,
                        0);


                parameters.addAll(
                    expander.expand(
                        new ExpansionContext("", alternate, context)));
            }
        }

        return parameters.stream()
                .filter(hiddenParameter().negate())
                .collect(toList());
    }

    private Predicate<Compatibility<springfox.documentation.service.Parameter, RequestParameter>> hiddenParameter() {
        return c -> c.getLegacy()
                .map(springfox.documentation.service.Parameter::isHidden)
                .orElse(false);
    }

}
