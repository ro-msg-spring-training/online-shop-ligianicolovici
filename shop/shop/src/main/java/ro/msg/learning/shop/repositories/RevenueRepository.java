package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Revenue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RevenueRepository extends JpaRepository<Revenue,Integer> {
    public List<Revenue> findAllByDate(LocalDate givenDate);
    public Optional<Revenue>findById(Integer revenueId);
}
