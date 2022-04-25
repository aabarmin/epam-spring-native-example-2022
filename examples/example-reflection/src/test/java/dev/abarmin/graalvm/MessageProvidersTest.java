package dev.abarmin.graalvm;

import java.util.function.Supplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Aleksandr Barmin
 */
public class MessageProvidersTest {
  @CsvSource(value = {
      "dev.abarmin.graalvm.HelloWorldProvider;Hello, World!",
      "dev.abarmin.graalvm.TestMessageProvider;Hello from test"
  }, delimiterString = ";")
  @ParameterizedTest
  void test(final String className, final String expectedMessage) throws Exception {
    final Class<?> aClass = Class.forName(className);
    final Object newInstance = aClass.getDeclaredConstructor().newInstance();
    final Supplier<String> supplier = Supplier.class.cast(newInstance);

    assertEquals(expectedMessage, supplier.get());
  }
}
