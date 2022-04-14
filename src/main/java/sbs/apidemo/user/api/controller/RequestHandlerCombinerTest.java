package sbs.apidemo.user.api.controller;

import com.fasterxml.classmate.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.service.RequestHandlerCombiner;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.BuilderDefaults.nullToEmptyList;

@Component
public class RequestHandlerCombinerTest implements RequestHandlerCombiner {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandlerCombinerTest.class);
    private TypeResolver typeResolver;

    public RequestHandlerCombinerTest(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Override
    public List<RequestHandler> combine(List<RequestHandler> source) {
        List<RequestHandler> combined = new ArrayList<RequestHandler>();
        for (RequestHandler each : nullToEmptyList(source)) {
            List<ResolvedMethodParameter> parameters = each.getParameters();
            for (ResolvedMethodParameter parameter : parameters) {
                System.out.println("parameter =  " + parameter);

            }
            /**
             * RequestMappingInfo(@Nullable String name,
             *                        @Nullable PathPatternsRequestCondition pathPatternsCondition,
             *            @Nullable PatternsRequestCondition patternsCondition,
             * 			RequestMethodsRequestCondition methodsCondition, ParamsRequestCondition paramsCondition,
             * 			HeadersRequestCondition headersCondition, ConsumesRequestCondition consumesCondition,
             * 			ProducesRequestCondition producesCondition, RequestConditionHolder customCondition,
             * 			BuilderConfiguration options) {
             *
             * 	org.springframework.web.servlet.mvc.method
             */

            Object[] objectsPatterns = each.getPatternsCondition().getPatterns().toArray();
            String[] patterns = new String[objectsPatterns.length];
            for (int i = 0; i < objectsPatterns.length; i++) {
                patterns[i] = objectsPatterns[i].toString();
            }
            PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(patterns, false, new AntPathMatcher());

            Object[] requestMethods = each.supportedMethods().toArray();
            for (Object requestMethod : requestMethods) {
                System.out.println("requestMethod = " + requestMethod);
            }

            RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(new RequestMethod[]{RequestMethod.POST});
            RequestMappingInfo requestMappingInfo = new RequestMappingInfo(
                    "",
                    null,
                    patternsRequestCondition,
                    requestMethodsRequestCondition,
                    new ParamsRequestCondition(""),
                    new HeadersRequestCondition(""),
                    new ConsumesRequestCondition(""),
                    new ProducesRequestCondition(""),
                    new RequestConditionHolder(null),
                    new RequestMappingInfo.BuilderConfiguration()
            );

            System.out.println("test");
//            new WebMvcRequestHandler("", new HandlerMethodResolver(typeResolver), )
        }

        return source;
    }
}
