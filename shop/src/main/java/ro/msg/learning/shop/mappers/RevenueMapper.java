package ro.msg.learning.shop.mappers;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.RevenueDTO;
import ro.msg.learning.shop.entities.Revenue;

import java.util.ArrayList;
import java.util.List;

@Component
public class RevenueMapper {
    public RevenueDTO revenueToRevenueDTO(Revenue revenue){
        return RevenueDTO.builder()
                .id(revenue.getId())
                .date(revenue.getDate())
                .sum(revenue.getSum())
                .locationId(revenue.getLocation().getId())
                .build();
    }
    public List<RevenueDTO> revenueListToRevenueDTOList(List<Revenue>revenueList){
        List<RevenueDTO> result= new ArrayList<>();
        for(Revenue revenue:revenueList){
            result.add(revenueToRevenueDTO(revenue));
        }
        return result;
    }
}
