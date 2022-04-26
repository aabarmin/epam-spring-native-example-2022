package dev.abarmin.graalvm;

import org.springframework.aot.context.bootstrap.generator.bean.descriptor.BeanInstanceDescriptor;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.BeanNativeConfigurationProcessor;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeConfigurationRegistry;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeProxyEntry;

/**
 * @author Aleksandr Barmin
 */
public class MyBeanNativeConfigurationProcessor implements BeanNativeConfigurationProcessor {
  @Override
  public void process(BeanInstanceDescriptor descriptor, NativeConfigurationRegistry registry) {
    if (descriptor.getUserBeanClass().getAnnotation(LogAroundMethod.class) != null) {
      final Class<?>[] interfaces = descriptor.getUserBeanClass().getInterfaces();
      registry.proxy().add(NativeProxyEntry.ofInterfaces(interfaces));
    }
  }
}
