package space.itzkana.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class NoInheritInterceptor implements MethodInterceptor {


    public NoInheritInterceptor() {
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.printf("before execute %s%n", method.getName());
        Object result = proxy.invokeSuper(obj, args);
        System.out.printf("after  execute %s, return value = %s%n", method.getName(), result.toString());
        return result;
    }
}
