package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Customer;

import java.util.Optional;

@Component
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Optional<Customer> findByUsername(String username);
}
