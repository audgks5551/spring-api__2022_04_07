package sbs.apidemo.base.swagger;

import com.fasterxml.classmate.TypeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.ServletContext;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class SpringFoxConfig {

    private ServletContext servletContext;
    private final ChangeModel changeModel;
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.ant(servletContext.getContextPath() + "/api/**"))
                .paths(PathSelectors.any())
                .build();

    }
}