package space.itzkana.service;

import org.springframework.stereotype.Service;

@Service
public class Inherit implements AnInterface {
    @Override
    public void retVoid() {
        internalInvoke();
    }

    @Override
    public int retNum() {
        internalInvoke();
        return (int) (Math.random() * 100);
    }

    @Override
    public void throwEx() {
        internalInvoke();
        throw new RuntimeException("throw runtime exception");
    }

    void internalInvoke() {
        Thread currentThread = Thread.currentThread();
        StackTraceElement[] stackTraceElements = currentThread.getStackTrace();
        String className = stackTraceElements[2].getClassName();
        String method = stackTraceElements[2].getMethodName();
        System.out.printf("call %s#%s%n", className, method);
    }
}
