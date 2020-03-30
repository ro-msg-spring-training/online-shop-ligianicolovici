package ro.msg.learning.shop.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Customer;

@Component()
public interface CustomerMongoRepository extends MongoRepository<Customer, String> {
    Customer findByUsername(String username);
}
