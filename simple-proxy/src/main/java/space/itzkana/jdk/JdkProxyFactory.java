package space.itzkana.jdk;

import space.itzkana.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JdkProxyFactory implements ProxyFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> T createProxy(T target) {
        Class<?> type = target.getClass();
        ClassLoader classLoader = type.getClassLoader();
        Class<?>[] interfaces = type.getInterfaces();
        InvocationHandler handler = new TargetInvocationHandler(target);
        return (T) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }
}
