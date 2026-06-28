package com.trainshier.controller;

import org.springframework.web.bind.annotation.*;

import com.trainshier.dto.CustomerResponseDTO;
import com.trainshier.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * @return customer
     */
    @GetMapping("/random")
    public CustomerResponseDTO randomCustomer() {

        return customerService.generateRandomCustomer();
    }
}