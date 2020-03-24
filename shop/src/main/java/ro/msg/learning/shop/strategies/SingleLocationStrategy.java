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
        List<Location> locationsWithAvailableStocks = stockRepository.findAllLocationsWithStocksByProductIdIn(getOrderedProductsIDsList(orderDetailDTOList));
        if (!locationsWithAvailableStocks.isEmpty()) {
            return getAndUpdateAvailableStocks(locationsWithAvailableStocks, orderDetailDTOList);
        }
        throw new ProductsCantBeShipped("Demanded products can't be taken from single location!");

    }

    public List<StockDTO> getAndUpdateAvailableStocks(List<Location> locationsWithAvailableStocks, List<OrderDetailDTO> orderDetailDTOList) {
        List<StockDTO> resultStockList = new ArrayList<>();
        List<Stock> stocks = stockRepository.findAllByLocationIdAndProductIdIn(locationsWithAvailableStocks.get(0).getId(), getOrderedProductsIDsList(orderDetailDTOList));
        for (Stock crtStock : stocks) {
            for (OrderDetailDTO orderProduct : orderDetailDTOList) {
                if (crtStock.getProduct().getId().equals(orderProduct.getProductId()) && orderProduct.getQuantity() <= crtStock.getQuantity()) {
                    resultStockList.add(StockDTO.builder()
                            .id(crtStock.getId())
                            .productId(orderProduct.getProductId())
                            .locationId(crtStock.getLocation().getId())
                            .quantity(orderProduct.getQuantity())
                            .build());
                    if (resultStockList.size() == orderDetailDTOList.size()) {
                        updateStocks(resultStockList);
                        return resultStockList;
                    }
                }
            }
        }throw new ProductsCantBeShipped("Demanded products can't be taken from single location!");
    }

    public void updateStocks(List<StockDTO> resultStocks) {
        for (StockDTO stockDTO : resultStocks)
            stockService.updateStock(stockRepository.findById(stockDTO.getId()).get(), stockDTO.getQuantity());
    }

    public List<Integer> getOrderedProductsIDsList(List<OrderDetailDTO> orderDetailDTOList) {
        List<Integer> orderedProductIds = new ArrayList<>();
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            orderedProductIds.add(orderDetailDTO.getProductId());
        }
        return orderedProductIds;
    }
}
