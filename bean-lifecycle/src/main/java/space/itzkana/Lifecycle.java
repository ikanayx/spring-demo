package space.itzkana;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Lifecycle {
    public static void main(String[] args) {
        System.out.println("init context");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("space.itzkana");

        MyBean mb = context.getBean("myBean", MyBean.class);
        System.out.println(mb);

        context.registerShutdownHook();
    }
}
