package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.entities.Order;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
