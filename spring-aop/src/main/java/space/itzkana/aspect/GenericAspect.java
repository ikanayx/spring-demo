package space.itzkana.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class GenericAspect {

    public void doBefore() {
        info("before");
    }

    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String method = signature.getName();
        info("call %s#%s".formatted(className, method));

        info("around before");

        Object output;
        try {
            output = joinPoint.proceed();
        } finally {
            info("around after");
            System.out.println();
        }
        return output;
    }

    public void doAfter() {
        info("after");
    }

    public void doAfterReturning(Object returnValue) {
        info("afterReturning, returnValue=" + returnValue);
    }

    public void doAfterThrow(Exception exception) {
        error("afterThrow, exception=" + exception);
        // exception.printStackTrace();
    }

    void info(String msg) {
        System.out.printf("%s: %s%n", getClass().getSimpleName(), msg);
    }

    void error(String msg) {
        // System.err.printf("%s: %s%n", getClass().getSimpleName(), msg);
        System.out.printf("%s: %s%n", getClass().getSimpleName(), msg);
    }
}
