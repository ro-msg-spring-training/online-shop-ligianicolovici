package ro.msg.learning.shop.strategies;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.StockService;
import ro.msg.learning.shop.utils.LocationFormatMapQuest;

import java.util.*;

@Component
public interface StrategyChoiceInterface {
    List<StockDTO> implementStrategy(List<OrderDetailDTO> orderDetailDTOList, LocationFormatMapQuest deliveryAddress);

    default Map<Integer, Integer> getOrderedProductsIDsAndQuantitiesMap(List<OrderDetailDTO> orderDetailDTOList) {
        Map<Integer, Integer> orderedProductIdsAndQuantities = new HashMap<>();
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            orderedProductIdsAndQuantities.put(orderDetailDTO.getProductId(), orderDetailDTO.getQuantity());
        }
        return orderedProductIdsAndQuantities;
    }

    default List<Integer> getOrderedProductsIDsList(List<OrderDetailDTO> orderDetailDTOList) {
        List<Integer> orderedProductIds = new ArrayList<>();
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            orderedProductIds.add(orderDetailDTO.getProductId());
        }
        return orderedProductIds;
    }

    default List<StockDTO> getAndUpdateAvailableStocks(List<Stock> stocks, List<OrderDetailDTO> orderDetailDTOList, StockRepository stockRepositoryOptional, StockService stockServiceOptional) {
        List<StockDTO> resultStockList = new ArrayList<>();
        for (Stock crtStock : stocks) {
            for (OrderDetailDTO orderProduct : orderDetailDTOList) {
                if (orderProduct.getProductId().equals(crtStock.getProduct().getId())) {
                    resultStockList.add(StockDTO.builder()
                            .id(crtStock.getId())
                            .productId(orderProduct.getProductId())
                            .locationId(crtStock.getLocation().getId())
                            .quantity(orderProduct.getQuantity())
                            .build());
                    if (resultStockList.size() == orderDetailDTOList.size()) {
                        updateStocks(resultStockList, stockRepositoryOptional, stockServiceOptional);
                        return resultStockList;
                    }
                }
            }
        }
        throw new ProductsCantBeShipped("Demanded products can't be taken from single location!");
    }

    default void updateStocks(List<StockDTO> resultStocks, StockRepository stockRepository, StockService stockService) {
        for (StockDTO stockDTO : resultStocks) {
            Optional<Stock> toUpdate = stockRepository.findById(stockDTO.getId());
            toUpdate.ifPresent(stock -> stockService.updateStock(stock, stockDTO.getQuantity()));
        }
    }
}
