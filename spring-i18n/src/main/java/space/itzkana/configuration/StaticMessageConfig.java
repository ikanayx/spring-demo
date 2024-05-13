package space.itzkana.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.StaticMessageSource;

@Configuration
public class StaticMessageConfig {

    @Bean
    public MessageSource messageSource() {
        return new StaticMessageSource();
    }
}
