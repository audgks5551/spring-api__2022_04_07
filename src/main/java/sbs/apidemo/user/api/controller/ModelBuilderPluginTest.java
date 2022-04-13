package sbs.apidemo.user.api.controller;

import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;

@Component
public class ModelBuilderPluginTest implements ModelBuilderPlugin {
    @Override
    public void apply(ModelContext context) {
        System.out.println("context = " + context);
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
