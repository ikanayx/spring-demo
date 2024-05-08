package space.itzkana.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TargetInvocationHandler implements InvocationHandler {
    private final Object target;

    public TargetInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.printf("before execute %s%n", method.getName());

        try {
            Object result = method.invoke(target, args);
            System.out.printf("after  execute %s, return value = %s%n", method.getName(), result.toString());
            return result;
        } catch (Exception e) {
            System.out.printf("had exception  %s, errorMsg = %s%n", method.getName(), e.getMessage());
            throw e;
        }
    }
}
