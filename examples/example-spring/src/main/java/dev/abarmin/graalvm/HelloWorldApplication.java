package dev.abarmin.graalvm;

import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Aleksandr Barmin
 */
@SpringBootApplication
public class HelloWorldApplication implements ApplicationRunner {
  public static void main(String[] args) {
    SpringApplication.run(HelloWorldApplication.class, args);
  }

  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    final long count = customerRepository.count();
    if (count > 0) {
      return;
    }

    Stream.of("Aleks",
        "Catherine",
        "Deepa",
        "Sanaz")
        .map(name -> {
          final Customer customer = new Customer();
          customer.setName(name);
          return customer;
        })
        .forEach(customerRepository::save);
  }
}
