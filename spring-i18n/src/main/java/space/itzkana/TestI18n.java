package space.itzkana;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import space.itzkana.configuration.StaticMessageConfig;
import space.itzkana.listener.CtxLoadedListener;

import java.util.Locale;

public class TestI18n {

    static Locale[] onlyLang = new Locale[]{Locale.CHINESE, Locale.JAPANESE, Locale.ENGLISH};
    static Locale[] locales = new Locale[]{
            Locale.SIMPLIFIED_CHINESE,
            Locale.TRADITIONAL_CHINESE, //Locale.TAIWAN,
            Locale.JAPAN, Locale.US, Locale.UK,
    };

    public static void main(String[] args) {
        testResourceBundleMessageSource(locales);
        testStaticMessageSource(new Locale[]{Locale.FRANCE});
    }

    public static void testStaticMessageSource(Locale[] locales) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(StaticMessageConfig.class);
        context.addApplicationListener(new CtxLoadedListener());
        context.refresh();

        for (Locale locale : locales) {
            var msg = context.getMessage("label.args", new Object[]{"foo", "bar"}, locale);
            System.out.printf("locale=%s, value=%s%n", locale, msg);
        }
    }

    public static void testResourceBundleMessageSource(Locale[] locales) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        for (Locale locale : locales) {
            //var msg = context.getMessage("label.name", null, locale);
            var msg = context.getMessage("label.args", new Object[]{"foo", "bar"}, locale);
            System.out.printf("locale=%s, value=%s%n", locale, msg);
        }
    }
}
