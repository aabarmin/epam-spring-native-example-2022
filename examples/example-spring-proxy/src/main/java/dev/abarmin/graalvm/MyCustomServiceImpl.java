package dev.abarmin.graalvm;

import org.springframework.stereotype.Service;

/**
 * @author Aleksandr Barmin
 */
@Service
@LogAroundMethod
public class MyCustomServiceImpl implements MyCustomService {
  @Override
  public void doSomething() {
    System.out.println("Hello, World!");
  }
}
