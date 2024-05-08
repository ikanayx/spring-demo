package space.itzkana;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class MyBean implements BeanFactoryAware, BeanNameAware, BeanClassLoaderAware, ApplicationContextAware, EnvironmentAware, InitializingBean, DisposableBean {

    private Long id;
    private String uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String toString() {
        return "MyBean(id=" + id + ",uuid=" + uuid + ")";
    }

    public MyBean() {
        System.out.println("execute MyBean#constructor");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("execute BeanClassLoaderAware#setBeanClassLoader");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("execute BeanFactoryAware#setBeanFactory");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("execute BeanNameAware#setBeanName");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("execute @PostConstruct CommonAnnotationBeanPostProcessor#postProcessBeforeInitializing");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("execute InitializeBean#afterPropertiesSet");
    }

    public void doInit() {
        System.out.println("execute init-method#doInit");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("execute ApplicationContextAware#setApplicationContext");
    }

    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("execute EnvironmentAware#setEnvironment");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("execute DisposeableBean#destroy");
    }

    public void doDestroy() {
        System.out.println("execute destroy-method#doInit");
    }
}
