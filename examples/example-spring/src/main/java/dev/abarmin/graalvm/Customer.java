package dev.abarmin.graalvm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author Aleksandr Barmin
 */
@Data
@Entity
@Table(name = "customers")
public class Customer {
  @Id
  @Column(name = "customer_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "customer_name")
  private String name;
}
