package dev.abarmin.graalvm;

import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeConfigurationRegistry;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeProxyEntry;
import org.springframework.nativex.AotOptions;
import org.springframework.nativex.type.NativeConfiguration;

/**
 * @author Aleksandr Barmin
 */
public class MyCustomBeanFactoryNativeConfigurationProcessor implements NativeConfiguration {
  @Override
  public void computeHints(NativeConfigurationRegistry registry, AotOptions aotOptions) {
    registry.proxy().add(NativeProxyEntry.ofInterfaces(
        MyCustomService.class
    ));
  }
}
