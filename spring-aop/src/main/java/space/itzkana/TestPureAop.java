package space.itzkana;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import space.itzkana.service.AnInterface;
import space.itzkana.service.Inherit;

public class TestPureAop {

    public static void main(String[] args) {
        AnInterface target = new Inherit();

        // ProxyFactoryBean
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.setTarget(target);
        factory.addInterface(AnInterface.class);

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* space.itzkana.service.Inherit.retVoid(..))");

        Advice advice = (MethodInterceptor) invocation -> {
            System.out.println("around before");
            Object result = invocation.proceed();
            System.out.println("around after");
            return result;
        };

        Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        factory.addAdvisor(advisor);
        AnInterface proxy1 = (AnInterface) factory.getObject();
        assert proxy1 != null;
        proxy1.retNum();
        proxy1.retVoid();

        // ProxyFactory
        ProxyFactory pf = new ProxyFactory(target);
        pf.addInterface(AnInterface.class);
        pf.addAdvice((AfterReturningAdvice) (returnValue, method, args1, target1) -> System.out.println("returnValue: " + returnValue));
        AnInterface proxy0 = (AnInterface) pf.getProxy();
        proxy0.retNum();

        // AspectJProxyFactory
        AspectJProxyFactory apf = new AspectJProxyFactory(target);
        apf.addAspect(SimpleAspect.class);
        AnInterface proxy = apf.getProxy();
        proxy.retNum();
    }

    @Aspect
    public static class SimpleAspect {
        @Before("execution(* space.itzkana.service.Inherit.*(..))")
        public void before() {
            System.out.println("before");
        }
    }
}
