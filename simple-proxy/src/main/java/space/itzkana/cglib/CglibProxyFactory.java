package space.itzkana.cglib;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;
import space.itzkana.ProxyFactory;

import java.io.Serializable;
import java.lang.reflect.Method;

public class CglibProxyFactory implements ProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T createProxy(T target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallbacks(new Callback[]{
                new NoInheritInterceptor(),
                new SerializableNoOp(),
        });
        enhancer.setCallbackFilter(method -> method.getName().contains("skipUseFilter") ? 1 : 0);
        return (T) enhancer.create();
    }

    public static class SerializableNoOp implements NoOp, Serializable {
    }
}
