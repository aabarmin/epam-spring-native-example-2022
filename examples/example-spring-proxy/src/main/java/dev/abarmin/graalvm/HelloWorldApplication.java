package dev.abarmin.graalvm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * Hello world!
 */
@SpringBootApplication
//@ImportRuntimeHints(MyCustomHints.class)
public class HelloWorldApplication implements ApplicationRunner {
  @Autowired
  private MyCustomService customService;

  public static void main(String[] args) {
    SpringApplication.run(HelloWorldApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    customService.doSomething();
  }
}
