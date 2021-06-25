package com.digitalacademy.customerservice.service;

import com.digitalacademy.customerservice.customer.CustomerSupportTest;
import com.digitalacademy.customerservice.model.Customer;
import com.digitalacademy.customerservice.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerService(customerRepository);
    }

    @DisplayName("Test get all customer should return list of customer")
    @Test
    void testGetAllCustomer() {
        when(customerRepository.findAll()).thenReturn(CustomerSupportTest.getCustomerList());
        List<Customer> resp = customerService.getCustomer();

        assertEquals(1, resp.get(0).getId().intValue());
        assertEquals("Ryan", resp.get(0).getFirstName());
        assertEquals("Gossing", resp.get(0).getLastName());
        assertEquals(12345666, resp.get(0).getPhoneNo().intValue());
        assertEquals("ryam@ga.com", resp.get(0).getEmail());

        assertEquals(2, resp.get(1).getId().intValue());
        assertEquals("Dave", resp.get(1).getFirstName());
        assertEquals("Sample", resp.get(1).getLastName());
        assertEquals(12345555, resp.get(1).getPhoneNo().intValue());
        assertEquals("Dave@ga.com", resp.get(1).getEmail());
    }

    @DisplayName("Test get customer by Id should return customer")
    @Test
    void testGetCustomerById() {
        Long reqParam = 10L;

        when(customerRepository.findAllById(reqParam))
                .thenReturn(CustomerSupportTest.getCustomerList().get(0));

        Customer resp = customerService.getCustomer(reqParam);

        assertEquals(1, resp.getId().intValue());
        assertEquals("Gossing", resp.getLastName());
        assertEquals(12345666, resp.getPhoneNo().intValue());
        assertEquals("ryam@ga.com", resp.getEmail());
    }

    @DisplayName("Test get customer by first name should return customer list")
    @Test
    void testGetCustomerByFirstName() {
        List<Customer> firstNameMatchList = new ArrayList<>();

        Customer c = new Customer();
        c.setId(11L);
        c.setFirstName("Frongsure");
        c.setLastName("Black");
        c.setEmail("fb@db.com");
        c.setPhoneNo(12345678);
        firstNameMatchList.add(c);

        c = new Customer();
        c.setId(12L);
        c.setFirstName("Frongsure");
        c.setLastName("White");
        c.setEmail("fb@db.com");
        c.setPhoneNo(12345678);
        firstNameMatchList.add(c);

        String firstNameReq = "Frongsure";
        when(customerRepository.findByFirstName(firstNameReq))
                .thenReturn(firstNameMatchList);

        List<Customer> resp = customerService.getCustomer(firstNameReq);
        assertEquals(11, resp.get(0).getId().intValue());
        assertEquals(12, resp.get(1).getId().intValue());

        assertEquals("Frongsure", resp.get(0).getFirstName());
        assertEquals("Frongsure", resp.get(1).getFirstName());

        assertEquals("Black", resp.get(0).getLastName());
        assertEquals("White", resp.get(1).getLastName());

        assertEquals("fb@db.com", resp.get(0).getEmail());
        assertEquals("fb@db.com", resp.get(1).getEmail());

        assertEquals(12345678, resp.get(0).getPhoneNo().intValue());
        assertEquals(12345678, resp.get(1).getPhoneNo().intValue());
    }

    @DisplayName("Test create customer should return customer")
    @Test
    void testCreateCustomer() {
        when(customerRepository.save(CustomerSupportTest.getCreateCustomer()))
                .thenReturn(CustomerSupportTest.getCreatedCustomer());

        Customer resp = customerService.createCustomer(CustomerSupportTest.getCreateCustomer());

        assertEquals(6, resp.getId().intValue());
        assertEquals("New", resp.getFirstName());
        assertEquals("Long", resp.getLastName());
        assertEquals(12345666, resp.getPhoneNo().intValue());
        assertEquals("New@ga.com", resp.getEmail());
    }

    @DisplayName("Test update customer should return success")
    @Test
    void testUpdateCustomer() {
        Long reqId = 3L;

        when(customerRepository.findAllById(reqId))
                .thenReturn(CustomerSupportTest.getBeforeUpdateCustomer());

        when(customerRepository.save(CustomerSupportTest.getAfterUpdateCustomer()))
                .thenReturn(CustomerSupportTest.getAfterUpdateCustomer());

        Customer resp = customerService.updateCustomer(reqId,
                CustomerSupportTest.getAfterUpdateCustomer());

        assertEquals(3, resp.getId().intValue());
        assertEquals("Old", resp.getFirstName());
        assertEquals("Too-old-ex", resp.getLastName());
        assertEquals(12345666, resp.getPhoneNo().intValue());
        assertEquals("Old@ga.com", resp.getEmail());
    }

    @DisplayName("Test update customer shold return fail")
    @Test
    void testUpdateCustimerFail() {
        Long reqId = 3L;

        when(customerRepository.findAllById(reqId)).thenReturn(null);

        Customer resp = customerService.updateCustomer(reqId,
                CustomerSupportTest.getAfterUpdateCustomer());

        assertEquals(null, resp);
    }

    @DisplayName("Test delete customer should return true")
    @Test
    void testDeleteCustomer() {
        Long reqId = 1L;
        doNothing().when(customerRepository).deleteById(reqId);
        boolean resp = customerService.deleteCustomer(reqId);

        assertEquals(true, resp);
        assertTrue(resp);
    }

    @DisplayName("Test delete customer should return false")
    @Test
    void testDeleteCustomerFail() {
        Long reqId = 1L;
        doThrow(EmptyResultDataAccessException.class)
                .when(customerRepository).deleteById(reqId);

        boolean resp = customerService.deleteCustomer(reqId);

        assertEquals(false, resp);
        assertFalse(resp);
    }
}
