package ro.msg.learning.shop.strategies;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.util.LocationFormatMapQuest;

import java.util.List;

@Component
public interface StrategyChoiceInterface {
    public List<StockDTO> implementStrategy(List<OrderDetailDTO> orderDetailDTOList, LocationFormatMapQuest deliveryAddress);
}
