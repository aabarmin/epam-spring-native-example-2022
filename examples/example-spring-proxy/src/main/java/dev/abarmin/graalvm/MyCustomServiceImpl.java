package dev.abarmin.graalvm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Aleksandr Barmin
 */
@Service
@LogAroundMethod
public class MyCustomServiceImpl implements MyCustomService {
  @Value("classpath:/resource-via-value.txt")
  private Resource resourceValue;

  @Override
  public void doSomething() {
    System.out.println("Hello, World!");

    // ---
    System.out.println("From resource:");
    System.out.println(readResource(resourceValue));

    // ---
    final ClassPathResource anotherResource = new ClassPathResource("resource-created-manually.txt");
    final String dynamicResourceName = readResource(anotherResource);
    System.out.println("From another resource:");
    System.out.println(dynamicResourceName);

    // ---
    final ClassPathResource dynamicResource = new ClassPathResource(dynamicResourceName);
    System.out.println("From dynamic resource:");
    System.out.println(readResource(dynamicResource));
  }

  private String readResource(final Resource resource) {
    try (final BufferedReader reader = new BufferedReader(
            new InputStreamReader(resource.getInputStream())
    )) {
      return reader.readLine();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
