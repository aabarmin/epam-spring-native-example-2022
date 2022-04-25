package dev.abarmin.graalvm;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Aleksandr Barmin
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
