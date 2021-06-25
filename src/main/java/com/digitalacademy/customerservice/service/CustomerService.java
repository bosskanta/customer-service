package com.digitalacademy.customerservice.service;

import com.digitalacademy.customerservice.model.Customer;
import com.digitalacademy.customerservice.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomer() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findAllById(id);
    }

    public List<Customer> getCustomer(String firstName) {
        return customerRepository.findByFirstName(firstName);
    }

    public Customer createCustomer(Customer body) {
        return customerRepository.save(body);
    }

    public Customer updateCustomer(Long id, Customer customer) {
        return customerRepository.findAllById(id) != null ?
                customerRepository.save(customer) :
                null;
    }

    public boolean deleteCustomer(Long id) {
        try {
            customerRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
