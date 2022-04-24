package dev.abarmin.graalvm;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

/**
 * @author Aleksandr Barmin
 */
public class RuntimeReflectionConfigurationFeature implements Feature {
  @Override
  public void beforeAnalysis(BeforeAnalysisAccess access) {
    RuntimeReflection.register(HelloWorldProvider.class);
    RuntimeReflection.registerForReflectiveInstantiation(HelloWorldProvider.class);
    RuntimeReflection.register(HelloWorldProvider.class.getDeclaredMethods());
  }
}
