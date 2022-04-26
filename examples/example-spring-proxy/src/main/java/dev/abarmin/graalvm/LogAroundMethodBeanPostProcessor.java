package dev.abarmin.graalvm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.aopalliance.intercept.Interceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author Aleksandr Barmin
 */
@Component
public class LogAroundMethodBeanPostProcessor implements BeanPostProcessor {
  private final Map<String,  Class<?>> beans = new HashMap<>();

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (bean.getClass().getAnnotation(LogAroundMethod.class) != null) {
      beans.put(beanName, bean.getClass());
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (!beans.containsKey(beanName)) {
      return bean;
    }
    final Class<?> beanClass = beans.get(beanName);
    return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy, method, args) -> {
      try {
        System.out.println("Started at " + LocalDateTime.now());
        return method.invoke(bean, args);
      } finally {
        System.out.println("Ended at " + LocalDateTime.now());
      }
    });
  }
}
