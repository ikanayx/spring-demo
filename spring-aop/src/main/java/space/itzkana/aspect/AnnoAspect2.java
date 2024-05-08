package space.itzkana.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
//@EnableAspectJAutoProxy和xml的<aop:aspectj-autoproxy/>等效
//proxyTargetClass = true强制使用cglib代理，否则由spring自动选择
//@org.springframework.context.annotation.EnableAspectJAutoProxy(proxyTargetClass = true)
//@Component
@Order(2)
public class AnnoAspect2 extends GenericAspect {
    @Pointcut("execution(* space.itzkana.service.AnInterface.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore() {
        super.doBefore();
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.doAround(joinPoint);
    }

    @After("pointcut()")
    public void doAfter() {
        super.doAfter();
    }

    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void doAfterReturning(Object returnValue) {
        super.doAfterReturning(returnValue);
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "exception")
    public void doAfterThrow(Exception exception) {
        super.doAfterThrow(exception);
    }
}
