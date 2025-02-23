package com.bside.potenday.service;

import com.bside.potenday.entity.Customer;
import com.bside.potenday.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomersByName(String name) {
        return customerRepository.findByName(name);
    }
}
