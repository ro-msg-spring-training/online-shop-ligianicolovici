package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.repositories.CustomerRepository;
import ro.msg.learning.shop.utils.UserCredentials;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserCredentialsService {

    private final CustomerRepository customerRepository;

    public List<UserCredentials> getCredentialsForAllUsers() {
        final List<Customer> allCustomers = StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        final List<UserCredentials> allUserCredentials = new ArrayList<>();
        allCustomers.forEach(customer -> allUserCredentials.add(new UserCredentials(customer.getUsername(), customer.getPassword())));

        return allUserCredentials;
    }
}
