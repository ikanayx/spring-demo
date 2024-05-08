package space.itzkana.jdk;

public class TestJdkProxy {
    public static void main(String[] args) {
        AnInterface instance = new JdkProxyFactory().createProxy(new TargetImpl());
        System.out.println(instance.randInt(5));
    }
}
