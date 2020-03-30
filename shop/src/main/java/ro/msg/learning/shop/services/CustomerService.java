package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.CustomerDTO;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.mappers.CustomerMapper;
import ro.msg.learning.shop.repositories.CustomerMongoRepository;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.utils.CustomerPrinciple;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerMongoRepository customerMongoRepository;

    @Override
    public CustomerPrinciple loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepository.findByUsername(username);
        if (customer.isPresent()) {
            return new CustomerPrinciple(customer);
        } else {
            return null;
        }
    }

    public CustomerDTO getCustomerByUsername(String username) {
        Optional<Customer> customer = Optional.ofNullable(customerMongoRepository.findByUsername(username));
        if (customer.isPresent()) {
            return customerMapper.customerToCustomerDTO(customer.get());
        } else {
            Customer customerNotFound = new Customer(0, null, null, null, null, null, Collections.emptyList());
            return customerMapper.customerToCustomerDTO(customerNotFound);
        }
    }
}

