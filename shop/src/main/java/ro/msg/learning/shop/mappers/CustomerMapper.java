package ro.msg.learning.shop.mappers;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.CustomerDTO;
import ro.msg.learning.shop.entities.Customer;

@Component
public class CustomerMapper {
    public CustomerDTO customerToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .username(customer.getUsername())
                .email(customer.getEmailAddress())
                .id(customer.getId())
                .build();
    }
}
