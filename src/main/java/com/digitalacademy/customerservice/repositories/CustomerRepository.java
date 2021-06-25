package com.digitalacademy.customerservice.repositories;

import com.digitalacademy.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findAllById(Long id);
    List<Customer> findByFirstName(String firstName);
}
