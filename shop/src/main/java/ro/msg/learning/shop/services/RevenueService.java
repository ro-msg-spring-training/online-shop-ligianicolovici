package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.RevenueDTO;
import ro.msg.learning.shop.mappers.RevenueMapper;
import ro.msg.learning.shop.repositories.RevenueRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final RevenueRepository revenueRepository;
    private final RevenueMapper revenueMapper;

    public List<RevenueDTO> getAllRevenueForGivenDate(LocalDate date) {
        return revenueMapper.revenueListToRevenueDTOList(revenueRepository.findAllByDate(date));
    }
}
