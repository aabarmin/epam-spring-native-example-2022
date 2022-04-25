package dev.abarmin.graalvm;

import java.util.function.Supplier;

/**
 * @author Aleksandr Barmin
 */
public class TestMessageProvider implements Supplier<String> {
  @Override
  public String get() {
    return "Hello from test";
  }
}
