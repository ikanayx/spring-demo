package space.itzkana.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class CtxLoadedListener implements ApplicationListener<ContextRefreshedEvent> {

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        StaticMessageSource source = context.getBean("messageSource", StaticMessageSource.class);

        Map<String, String> i18nDynamicData = new HashMap<>();
        i18nDynamicData.put("label.name", "nom");
        i18nDynamicData.put("label.holiday", "vacances");
        i18nDynamicData.put("label.args", "Étiquette={0}, Valeur={1}");

        source.addMessages(i18nDynamicData, Locale.FRANCE);
    }
}
