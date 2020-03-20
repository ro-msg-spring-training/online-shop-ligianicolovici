package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.StockService;
import ro.msg.learning.shop.utils.LocationFormatMapQuest;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SingleLocationStrategy implements StrategyChoiceInterface {

    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final StockService stockService;


    @Override
    public List<StockDTO> implementStrategy(List<OrderDetailDTO> orderDetailDTOList, LocationFormatMapQuest deliveryAddress) {
        List<Location> allLocations = locationRepository.findAll();
        List<StockDTO> resultStockList = new ArrayList<>();

        for (Location location : allLocations) {
            List<Stock> localStock = stockRepository.findAllByLocationId(location.getId());

            resultStockList.clear();
            for (Stock crtStock : localStock) {
                for (OrderDetailDTO orderProduct : orderDetailDTOList) {
                    if (crtStock.getProduct().getId().equals(orderProduct.getProductId()) && orderProduct.getQuantity() <= crtStock.getQuantity()) {
                        resultStockList.add(
                                StockDTO.builder()
                                        .productId(orderProduct.getProductId())
                                        .locationId(location.getId())
                                        .quantity(orderProduct.getQuantity())
                                        .build()
                        );
                        stockService.updateStock(crtStock, orderProduct.getQuantity());
                        if (resultStockList.size() == orderDetailDTOList.size()) {
                            return resultStockList;
                        }
                    }
                }

            }

        }
        throw new ProductsCantBeShipped("Demanded products can't be taken from single location!");
    }

}
