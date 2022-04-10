package sbs.apidemo.base.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.MappingMediaTypeFileExtensionResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewRequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import sbs.apidemo.base.argumentresolver.dtotovo.DtoToVoArgumentResolver;
import sbs.apidemo.base.argumentresolver.votodto.VoToDtoArgumentResolver;
import sbs.apidemo.base.argumentresolver.login.LoginArgumentResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.MediaType.*;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ModelMapper modelMapper;
    private List<HttpMessageConverter<?>> converters;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        this.converters = converters;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(new LoginArgumentResolver());
        resolvers.add(new VoToDtoArgumentResolver(converters, getRequestResponseBodyAdvice(), modelMapper));
    }
    
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new DtoToVoArgumentResolver(converters, getContentNegotiationManager(), getRequestResponseBodyAdvice(), modelMapper));
    }

    private List<Object> getRequestResponseBodyAdvice() {
        /**
         * requestResponseBodyAdvice
         */
        List<Object> requestResponseBodyAdvice = new ArrayList<>();
        requestResponseBodyAdvice.add(new JsonViewRequestBodyAdvice());
        requestResponseBodyAdvice.add(new JsonViewResponseBodyAdvice());
        return requestResponseBodyAdvice;
    }

    private ContentNegotiationManager getContentNegotiationManager() {
        /**
         * mediaTypes
         */
        HashMap<String, MediaType> mediaTypes = new HashMap<>();
        mediaTypes.put("xml", new MediaType(APPLICATION_XML));
        mediaTypes.put("json", new MediaType(APPLICATION_JSON));

        /**
         * contentNegotiationManager
         */
        ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
        contentNegotiationManager.addFileExtensionResolvers(new MappingMediaTypeFileExtensionResolver(mediaTypes));

        return contentNegotiationManager;
    }
}
