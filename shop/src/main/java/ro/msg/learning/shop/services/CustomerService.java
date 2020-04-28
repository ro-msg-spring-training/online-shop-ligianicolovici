package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.exceptions.CustomerNotRegistered;
import ro.msg.learning.shop.mappers.CustomerMapper;
import ro.msg.learning.shop.repositories.CustomerMongoRepository;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.utils.CustomerPrinciple;

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

    public Integer getCustomerIdByUsername(String username) {
        Optional<Customer> result = customerRepository.findByUsername(username);
        if (result.isPresent()) {
            return result.get().getId();
        } else {
            throw new CustomerNotRegistered("Couldn't find customer!");
        }

    }

    public void registerNewCustomer(Customer customer) {
        customerRepository.save(customer);
    }

}

