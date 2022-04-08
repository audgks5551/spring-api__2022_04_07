package sbs.apidemo.base.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sbs.apidemo.base.argumentresolver.dto.DtoArgumentResolver;
import sbs.apidemo.base.argumentresolver.login.LoginArgumentResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    private final StringHttpMessageConverter stringHttpMessageConverter;
    private final ModelMapper modelMapper;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(mappingJackson2HttpMessageConverter);
        converters.add(stringHttpMessageConverter);

        resolvers.add(new LoginArgumentResolver());
        resolvers.add(new DtoArgumentResolver(converters, modelMapper));
    }
}
