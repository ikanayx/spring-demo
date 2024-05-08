package space.itzkana.cglib;

public class TestCglib {
    public static void main(String[] args) {
        NoInherit instance = new CglibProxyFactory().createProxy(new NoInherit());
        System.out.println(instance.randInt());
        System.out.println(instance.skipUseFilter());
        System.out.println(instance.skipFinal());
    }
}
