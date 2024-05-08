package space.itzkana;

public interface ProxyFactory {
    <T> T createProxy(T target);
}
