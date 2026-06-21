package com.trainshier.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.trainshier.dto.CustomerResponseDTO;
import com.trainshier.entity.CustomerProfile;
import com.trainshier.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponseDTO generateRandomCustomer() {

        List<CustomerProfile> customers =
                customerRepository.findAll();

        if (customers.isEmpty()) {
            throw new RuntimeException(
                    "No customer profiles found"
            );
        }

        CustomerProfile customer =
                customers.get(
                        new Random().nextInt(customers.size())
                );

        CustomerResponseDTO response =
                new CustomerResponseDTO();

        response.setName(customer.getName());
        response.setMood(customer.getMood());
        response.setPatience(customer.getPatience());
        response.setRequest(customer.getRequestText());
        response.setMessage(customer.getMessage());

        return response;
    }
}