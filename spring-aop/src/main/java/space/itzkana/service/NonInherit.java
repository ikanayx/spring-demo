package space.itzkana.service;

public class NonInherit {

    public void retVoid() {
        internalInvoke();
    }

    public int retNum() {
        internalInvoke();
        return (int) (Math.random() * 100);
    }

    public void throwEx() {
        internalInvoke();
        throw new RuntimeException("throw runtime exception");
    }

    void internalInvoke() {
        Thread currentThread = Thread.currentThread();
        StackTraceElement[] stackTraceElements = currentThread.getStackTrace();
        String className = stackTraceElements[2].getClassName();
        String method = stackTraceElements[2].getMethodName();
        System.out.printf("%s#%s: Method internal call.%n", className, method);
    }
}
