package sbs.apidemo.base.swagger;

import com.fasterxml.classmate.TypeResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sbs.apidemo.base.argumentresolver.votodto.VoToDto;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ParameterStyle;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import java.util.Optional;
import static springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER;

@Slf4j
@Component
@Order(SWAGGER_PLUGIN_ORDER)
public class ChangeModel implements ParameterBuilderPlugin {

    private TypeResolver resolver;

    public ChangeModel(TypeResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        Optional<VoToDto> requestParam = methodParameter.findAnnotation(VoToDto.class);
        if (requestParam.isPresent()) {
            parameterContext.requestParameterBuilder()
                    .in(ParameterType.HEADER)
                    .name("v")
                    .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)));
            parameterContext.requestParameterBuilder()
                    .in(ParameterType.HEADER)
                    .name("v")
                    .query(q -> q.style(ParameterStyle.SIMPLE)
                            .model(m -> m.scalarModel(ScalarType.STRING)));
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
