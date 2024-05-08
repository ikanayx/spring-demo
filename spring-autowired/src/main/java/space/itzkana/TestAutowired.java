package space.itzkana;

import space.itzkana.service.CompAImpl;
import space.itzkana.service.ICompA;
import space.itzkana.service.InjectedService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestAutowired {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("space.itzkana");
        InjectedService service = context.getBean("injectedService", InjectedService.class);
        service.printAutowireCompName();
    }

    @Bean
    public ICompA iCompA() {
        return new CompAImpl("typeMatched and nameMatched");
    }

    @Bean
    public ICompA iCompA2() {
        return new CompAImpl("typeMatched but beanName = iCompA2");
    }

    @Bean
    @Primary
    public ICompA iCompAPrimary() {
        return new CompAImpl("primaryMatched");
    }
}
