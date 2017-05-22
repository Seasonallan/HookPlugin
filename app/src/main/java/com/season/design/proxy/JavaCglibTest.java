package com.season.design.proxy;

import com.mdit.library.proxy.CallbackFilter;
import com.mdit.library.proxy.Enhancer;
import com.mdit.library.proxy.MethodInterceptor;
import com.mdit.library.proxy.MethodProxy;
import com.season.test.SeasonApplication;

import java.lang.reflect.Method;

/**
 * 使用动态字节码生成技术实现AOP原理是在运行期间目标字节码加载后，生成目标类的子类，将切面逻辑加入到子类中，所以cglib实现AOP不需要基于接口
 * cglib是一个强大的、高性能的Code生成类库，它可以在运行期间扩展Java类和实现Java接口，它封装了Asm，所以使用cglib前需要引入Asm的jar
 * User: SeasonAllan(451360508@qq.com)
 * Time: 2017-05-15 11:39
 */
public class JavaCglibTest {

    public static void main(String[] args) {
        //需要上下文，无法测试
    }

    public static void test() {
        JavaCglibTest.BookFacadeCglib cglib = new JavaCglibTest.BookFacadeCglib();
        BookFacadeImpl bookFacade = (BookFacadeImpl) cglib.getInstance(new BookFacadeImpl());
        bookFacade.doSomeThing("仓鼠1");
        bookFacade.doSomeThing2("仓鼠1", "仓鼠33");
    }

    /**
     * 使用cglib动态代理
     */
    public static class BookFacadeCglib implements MethodInterceptor, CallbackFilter {

        private Object target;

        /**
         * 创建代理对象
         *
         * @param target
         * @return
         */
        public Object getInstance(Object target) {
            this.target = target;
            Enhancer enhancer = new Enhancer(SeasonApplication.sApplicationContext);
            enhancer.setSuperclass(this.target.getClass());
            //回调方法
            //  enhancer.setCallback(this);
            enhancer.setCallbackFilter(this);
            MethodInterceptor[] callbacks = new MethodInterceptor[2];
            callbacks[0] = this;
            callbacks[1] = new MethodInterceptor() {
                @Override
                public Object intercept(Object object, Object[] args, MethodProxy methodProxy) throws Exception {
                    System.out.println("执行方法 " + methodProxy.getMethodName());
                    for (Object arg : args) {
                        System.out.println("参数-->> " + arg);
                    }
                    System.out.println("不执行");
                    return null;
                }
            };
            enhancer.setCallbacks(callbacks);
            //创建代理
            return enhancer.create();
        }

        @Override
        public Object intercept(Object object, Object[] args, MethodProxy methodProxy) throws Exception {
            System.out.println("执行方法 " + methodProxy.getMethodName());
            for (Object arg : args) {
                System.out.println("参数-->> " + arg);
            }
            return methodProxy.invokeSuper(object, args);
        }

        @Override
        public int accept(Method method) {
            System.out.println("accept " + method.getName());
            if (method.getName().equals("doSomeThing2")) {
                return 0;
            }
            return 1;
        }
    }


    public static class BookFacadeImpl {

        public void doSomeThing(String tag) {
            System.out.println("执行业务逻辑");
        }

        public void doSomeThing2(String tag, String tag2) {
            System.out.println("执行业务逻辑2");
        }

    }
}
