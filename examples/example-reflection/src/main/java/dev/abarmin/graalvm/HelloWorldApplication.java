package dev.abarmin.graalvm;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * Hello world!
 */
public class HelloWorldApplication {
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("Class name is required");
      System.out.println("Example: dev.abarmin.graalvm.HelloWorldProvider");
      System.exit(1);
    }

    final String className = args[0];
    System.out.println("Class to be loaded: " + className);

    final Class<?> providerClass = Class.forName(className);
    System.out.println("Class is loaded");

    final Object newInstance = providerClass.getDeclaredConstructor().newInstance();
    System.out.println("Instance of the class created");

    final Supplier<String> helloWorldSupplier = Supplier.class.cast(newInstance);

    System.out.println("=====");
    System.out.println(helloWorldSupplier.get());
    System.out.println("=====");

    System.out.println("Declared methods:");
    for (Method method : providerClass.getDeclaredMethods()) {
      System.out.println(method.getDeclaringClass().getName() + " ->  " + method.getName());
    }
  }
}
