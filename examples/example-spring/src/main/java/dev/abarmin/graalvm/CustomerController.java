package dev.abarmin.graalvm;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aleksandr Barmin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
  private final CustomerRepository customerRepository;

  @GetMapping
  public Collection<Customer> findAll() {
    return customerRepository.findAll();
  }
}
