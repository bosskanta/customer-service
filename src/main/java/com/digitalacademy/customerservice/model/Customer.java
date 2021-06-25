package com.digitalacademy.customerservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100, message = "Please type your first name size between 1 - 100")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 100, message = "Please type your last name size between 1 - 100")
    private String lastName;

    @Email(message = "Please type valid email address")
    private String email;

    @NotNull
    @Min(value = 1, message = "Please type your Telephone number no > 0 digit")
    private Integer phoneNo;
}
