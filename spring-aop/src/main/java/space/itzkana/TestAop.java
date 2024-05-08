package space.itzkana;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import space.itzkana.service.Inherit;
import space.itzkana.service.NonInherit;
import space.itzkana.service.AnInterface;

public class TestAop {
    // 切面 aspect
    //   - 切入点 pointCut
    //     - 连接点 joinPoint
    //   - 通知 advice
    //     - 类型
    //       - 前置 before
    //       - 环绕 around
    //       - 后置 after
    //       - 返回值 afterReturning
    //       - 异常 afterThrowing
    // 通知执行器 advisor = aspect信息 + advice信息 + pointcut
    // 织入 weaving
    //   - 静态 AspectJ编译器
    //   - 动态 SpringAOP
    //     - JdkProxy 基于接口与反射
    //     - Cglib 基于类继承
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        NonInherit nonInherit = context.getBean(NonInherit.class);
        printBeanInfo(nonInherit);
        nonInherit.retVoid();
        nonInherit.retNum();
        try {
            nonInherit.throwEx();
        } catch (Exception ignored) {
        }

        AnInterface interface0 = context.getBean(AnInterface.class);
        printBeanInfo(interface0);
        interface0.retVoid();
        interface0.retNum();
        try {
            interface0.throwEx();
        } catch (Exception ignored) {
        }
    }

    static void printBeanInfo(Object bean) {
        boolean isProxyObj = AopUtils.isAopProxy(bean);
        boolean isJdkProxy = AopUtils.isJdkDynamicProxy(bean);
        String beanClassName = bean.getClass().getSimpleName();
        System.out.printf("%n%s, isProxyObj=%s, proxyType=%s%n", beanClassName, isProxyObj, isJdkProxy ? "JdkProxy" : "CGLIB");
    }
}
