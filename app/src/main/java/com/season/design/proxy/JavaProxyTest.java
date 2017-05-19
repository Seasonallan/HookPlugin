package com.season.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理：即在运行期动态创建代理类，使用动态代理实现AOP需要4个角色：
 * 被代理的类：即AOP里所说的目标对象
 * 被代理类的接口
 * 织入器：使用接口反射机制生成一个代理类，在这个代理类中织入代码
 * InvocationHandler切面：切面，包含了Advice和Pointcut
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-15 11:39
 */
public class JavaProxyTest {

    public static void main(String[] args) {
        test();
    }

    public static void test(){
        //需要代理的类接口，被代理类实现的多个接口都必须在这这里定义
        Class[] proxyInterface = new Class[]{IBusiness.class, IBusiness2.class};
        //构建AOP的Advice，这里需要传入业务类的实例
        LogInvocationHandler handler = new LogInvocationHandler(new Business());
        //生成代理类的字节码加载器
        ClassLoader classLoader = Business.class.getClassLoader();
        //织入器，织入代码并生成代理类
        IBusiness2 proxyBusiness = (IBusiness2) Proxy.newProxyInstance(classLoader, proxyInterface, handler);
        proxyBusiness.doSomeThing2();
        ((IBusiness) proxyBusiness).doSomeThing();
    }

    public static class LogInvocationHandler implements InvocationHandler {

        private Object target;//目标对象

        public LogInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //执行织入的日志，你可以控制哪些方法执行切入逻辑
            if (method.getName().equals("doSomeThing2")) {
                System.out.println("记录日志 doSomeThing2");
            }
            if (method.getName().equals("doSomeThing")) {
                System.out.println("记录日志  doSomeThing");
            }
            //执行原有逻辑
            Object recv = method.invoke(target, args);
            return recv;
        }
    }

    public interface IBusiness {
        void doSomeThing();
    }

    public interface IBusiness2 {
        void doSomeThing2();
    }

    public static class Business implements IBusiness, IBusiness2 {
        @Override
        public void doSomeThing() {
            System.out.println("执行业务逻辑");
        }

        @Override
        public void doSomeThing2() {
            System.out.println("执行业务逻辑2");
        }

        public void doSomeThing3() {
            System.out.println("执行业务逻辑3");
        }
    }
}
