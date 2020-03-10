package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.StockService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SingleLocationStrategy implements StrategyChoiceInterface {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private StockService stockService;


    @Override
    public List<StockDTO> implementStrategy(List<OrderDetailDTO> orderDetailDTOList) {
        List<Location> allLocations = locationRepository.findAll();
        List<StockDTO> resultStockList = new ArrayList<>();

        for (Location location : allLocations) {
            List<Stock> localStock = stockRepository.findAllByLocation_Id(location.getId());

            resultStockList.clear();

            for (Stock crtStock : localStock) {
                for (OrderDetailDTO orderProduct : orderDetailDTOList) {
                    if (crtStock.getProduct().getId().equals(orderProduct.getProductId()) && orderProduct.getQuantity() <= crtStock.getQuantity()) {
                        resultStockList.add(
                                StockDTO.builder()
                                        .product_id(orderProduct.getProductId())
                                        .location_id(location.getId())
                                        .quantity(orderProduct.getQuantity())
                                        .build()
                        );
                        stockService.updateStock(orderProduct.getProductId(), location.getId(), crtStock, orderProduct.getQuantity());

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
