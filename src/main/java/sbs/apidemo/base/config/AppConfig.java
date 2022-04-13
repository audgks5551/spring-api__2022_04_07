package sbs.apidemo.base.config;

import com.fasterxml.classmate.TypeResolver;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import sbs.apidemo.base.swagger.ChangeModel;

@Configuration
public class AppConfig {

    /**
     * matching 전략 엄격하게
     */
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }
}
