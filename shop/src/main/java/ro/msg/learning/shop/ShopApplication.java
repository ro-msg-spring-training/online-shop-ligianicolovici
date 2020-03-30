package ro.msg.learning.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import ro.msg.learning.shop.entities.Customer;
import ro.msg.learning.shop.repositories.CustomerMongoRepository;

import java.util.Collections;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class ShopApplication implements CommandLineRunner {

    private final CustomerMongoRepository customerMongoRepository;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        customerMongoRepository.save(new Customer(0,"mongo_test_user1","db",null,null,null, Collections.emptyList()));
        customerMongoRepository.save(new Customer(1,"mongo_test_user2","db","test",null,"test@msg.group", Collections.emptyList()));

    }
}
