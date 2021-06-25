package com.digitalacademy.customerservice.controller;

import com.digitalacademy.customerservice.model.Customer;
import com.digitalacademy.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/list")
    public List<Customer> getAllCustomer() {
        return customerService.getCustomer();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomer(id);
        return customer != null ?
                ResponseEntity.ok(customer) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getCustomerByName(@PathVariable String name) {
        List<Customer> cl = customerService.getCustomer(name);
        return !cl.isEmpty() ?
                ResponseEntity.ok(cl) :
                ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer body) {
        Customer customer = customerService.createCustomer(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putCustomer(@PathVariable Long id, @Valid @RequestBody Customer body) {
        body.setId(id);
        Customer customer = customerService.updateCustomer(id, body);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
}
