package com.digitalacademy.customerservice.customer;

import com.digitalacademy.customerservice.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerSupportTest {
    public static List<Customer> getCustomerList() {
        List<Customer> customerList = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Ryan");
        customer.setLastName("Gossing");
        customer.setPhoneNo(12345666);
        customer.setEmail("ryam@ga.com");
        customerList.add(customer);

        customer = new Customer();
        customer.setId(2L);
        customer.setFirstName("Dave");
        customer.setLastName("Sample");
        customer.setPhoneNo(12345555);
        customer.setEmail("Dave@ga.com");
        customerList.add(customer);

        return customerList;
    }

    public static Customer getCreateCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("New");
        customer.setLastName("Long");
        customer.setPhoneNo(12345666);
        customer.setEmail("New@ga.com");
        return customer;
    }

    public static Customer getCreatedCustomer() {
        Customer customer = new Customer();
        customer.setId(6L);
        customer.setFirstName("New");
        customer.setLastName("Long");
        customer.setPhoneNo(12345666);
        customer.setEmail("New@ga.com");
        return customer;
    }

    public static Customer getBeforeUpdateCustomer() {
        Customer customer = new Customer();
        customer.setId(3L);
        customer.setFirstName("Old");
        customer.setLastName("Too-old");
        customer.setPhoneNo(12345666);
        customer.setEmail("Old@ga.com");
        return customer;
    }

    public static Customer getAfterUpdateCustomer() {
        Customer customer = new Customer();
        customer.setId(3L);
        customer.setFirstName("Old");
        customer.setLastName("Too-old-ex");
        customer.setPhoneNo(12345666);
        customer.setEmail("Old@ga.com");
        return customer;
    }
}
