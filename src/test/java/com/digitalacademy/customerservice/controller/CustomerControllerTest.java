package com.digitalacademy.customerservice.controller;

import com.digitalacademy.customerservice.customer.CustomerSupportTest;
import com.digitalacademy.customerservice.model.Customer;
import com.digitalacademy.customerservice.service.CustomerService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.print.attribute.standard.Media;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerControllerTest {
    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerController = new CustomerController(customerService);
        mvc = MockMvcBuilders.standaloneSetup(customerController).build();

        when(customerService.getCustomer()).thenReturn(CustomerSupportTest.getCustomerList());
    }

    @DisplayName("Test get customer should return customer list")
    @Test
    void testGetCustomerList() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/customer/list/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertEquals("1", jsonArray.getJSONObject(0).get("id").toString());
        assertEquals("Ryan", jsonArray.getJSONObject(0).get("firstName").toString());
        assertEquals("Gossing", jsonArray.getJSONObject(0).get("lastName").toString());
        assertEquals("ryam@ga.com", jsonArray.getJSONObject(0).get("email").toString());
        assertEquals("12345666", jsonArray.getJSONObject(0).get("phoneNo").toString());

        assertEquals("2", jsonArray.getJSONObject(1).get("id").toString());
        assertEquals("Dave", jsonArray.getJSONObject(1).get("firstName").toString());
        assertEquals("Sample", jsonArray.getJSONObject(1).get("lastName").toString());
        assertEquals("Dave@ga.com", jsonArray.getJSONObject(1).get("email").toString());
        assertEquals("12345555", jsonArray.getJSONObject(1).get("phoneNo").toString());

        verify(customerService, times(1)).getCustomer();
    }

    @DisplayName("Test get customer by id should return customer")
    @Test
    void testGetCustomerById() throws Exception {
        Long reqId = 4L;

        when(customerService.getCustomer(reqId)).thenReturn(CustomerSupportTest.getCustomerList().get(0));

        MvcResult mvcResult = mvc.perform(get("/customer/" + reqId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("1", jsonObject.get("id").toString());
        assertEquals("Ryan", jsonObject.get("firstName").toString());
        assertEquals("Gossing", jsonObject.get("lastName").toString());
        assertEquals("ryam@ga.com", jsonObject.get("email").toString());
        assertEquals("12345666", jsonObject.get("phoneNo").toString());

        verify(customerService, times(1)).getCustomer(reqId);
    }

    @DisplayName("Test get customer by id should return not found")
    @Test
    void testGetCustomerByIdNotFound() throws Exception {
        Long reqId = 5L;
        mvc.perform(get("/customer/" + reqId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @DisplayName("Test get customer by name should return customer list")
    @Test
    void testGetCustomerByName() throws Exception {
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

        when(customerService.getCustomer(firstNameReq)).thenReturn(firstNameMatchList);

        MvcResult mvcResult = mvc.perform(get("/customer/name/" + firstNameReq))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertEquals("11", jsonArray.getJSONObject(0).get("id").toString());
        assertEquals("Frongsure", jsonArray.getJSONObject(0).get("firstName").toString());
        assertEquals("Black", jsonArray.getJSONObject(0).get("lastName").toString());
        assertEquals("fb@db.com", jsonArray.getJSONObject(0).get("email").toString());
        assertEquals("12345678", jsonArray.getJSONObject(0).get("phoneNo").toString());

        assertEquals("12", jsonArray.getJSONObject(1).get("id").toString());
        assertEquals("Frongsure", jsonArray.getJSONObject(1).get("firstName").toString());
        assertEquals("White", jsonArray.getJSONObject(1).get("lastName").toString());
        assertEquals("fb@db.com", jsonArray.getJSONObject(1).get("email").toString());
        assertEquals("12345678", jsonArray.getJSONObject(1).get("phoneNo").toString());

        verify(customerService, times(1)).getCustomer(firstNameReq);
    }

    @DisplayName("Test get customer by name should return not found")
    @Test
    void testGetCustomerByNameNotFound() throws Exception {
        String firstNameReq = "AAA";

        when(customerService.getCustomer(firstNameReq)).thenReturn(new ArrayList<>());

        mvc.perform(get("/customer/name/" + firstNameReq))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @DisplayName("Test create customer should return customer")
    @Test
    void testCreateCustomer() throws Exception {
        Customer customerRequest = CustomerSupportTest.getCreateCustomer();

        when(customerService.createCustomer(customerRequest)).thenReturn(CustomerSupportTest.getCreatedCustomer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerRequest);

        MvcResult mvcResult = mvc.perform(post("/customer/")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("6", jsonObject.get("id").toString());
        assertEquals("New", jsonObject.get("firstName").toString());
        assertEquals("Long", jsonObject.get("lastName").toString());
        assertEquals("New@ga.com", jsonObject.get("email").toString());
        assertEquals("12345666", jsonObject.get("phoneNo").toString());

        verify(customerService, times(1)).createCustomer(customerRequest);
    }

    @DisplayName("Test create customer with name is empty should return error")
    @Test
    void testCreateCustomerWithNameEmpty() throws Exception {
        Customer customerRequest = CustomerSupportTest.getCreateCustomer();
        customerRequest.setFirstName("");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerRequest);

        when(customerService.createCustomer(customerRequest)).thenReturn(CustomerSupportTest.getCreatedCustomer());

        MvcResult mvcResult = mvc.perform(post("/customer/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(mvcResult);
    }

    @DisplayName("Test update customer should return customer")
    @Test
    void testUpdateCustomer() throws Exception {
        Customer customerRequest = CustomerSupportTest.getBeforeUpdateCustomer();
        Long reqId = 3L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerRequest);

        when(customerService.updateCustomer(reqId, customerRequest)).thenReturn(CustomerSupportTest.getAfterUpdateCustomer());

        MvcResult mvcResult = mvc.perform(put("/customer/" + reqId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("3", jsonObject.get("id").toString());
        assertEquals("Old", jsonObject.get("firstName").toString());
        assertEquals("Too-old-ex", jsonObject.get("lastName").toString());
        assertEquals("Old@ga.com", jsonObject.get("email").toString());
        assertEquals("12345666", jsonObject.get("phoneNo").toString());

        verify(customerService, times(1)).updateCustomer(reqId, customerRequest);
    }

    @DisplayName("Test update customer with id not found should return not found")
    @Test
    void testUpdateCustomerNotFound() throws Exception {
        Customer customerRequest = CustomerSupportTest.getBeforeUpdateCustomer();
        Long reqId = 3L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(customerRequest);

        when(customerService.updateCustomer(reqId, customerRequest)).thenReturn(null);

        MvcResult mvcResult = mvc.perform(put("/customer/" + reqId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(customerService, times(1)).updateCustomer(reqId, customerRequest);
    }

    @DisplayName("Test delete customer should return success")
    @Test
    void testDeleteCustomer() throws Exception {
        Long reqId = 4L;
        when(customerService.deleteCustomer(reqId)).thenReturn(true);

        MvcResult mvcResult = mvc.perform(delete("/customer/" + reqId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(customerService, times(1)).deleteCustomer(reqId);
    }

    @DisplayName("Test delete customer id not found should return not found")
    @Test
    void testDeleteCustomerIdNotFound() throws Exception {
        Long reqId = 4L;
        when(customerService.deleteCustomer(reqId)).thenReturn(false);

        MvcResult mvcResult = mvc.perform(delete("/customer/" + reqId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(customerService, times(1)).deleteCustomer(reqId);
    }
}
