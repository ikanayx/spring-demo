package space.itzkana.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.itzkana.MyBean;

@Configuration
public class BeanConfig {
    @Bean(initMethod = "doInit", destroyMethod = "doDestroy")
    public MyBean myBean() {
        MyBean b = new MyBean();
        b.setId(10001L);
        b.setUuid("78f9170b176841b594d34c76aa230c57");
        return b;
    }
}
