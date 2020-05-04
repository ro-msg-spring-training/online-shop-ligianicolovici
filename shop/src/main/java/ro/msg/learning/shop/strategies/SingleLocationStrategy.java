package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.LocationRepositoryCustom;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.repositories.StockRepositoryCustom;
import ro.msg.learning.shop.services.StockService;
import ro.msg.learning.shop.utils.LocationAndProductIDs;
import ro.msg.learning.shop.utils.LocationFormatMapQuest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SingleLocationStrategy implements StrategyChoiceInterface {

    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final StockMapper stockMapper;
    private final StockService stockService;
    private final StockRepositoryCustom stockRepositoryCustom;
    private final LocationRepositoryCustom locationRepositoryCustom;


    @Override
    public List<StockDTO> implementStrategy(List<OrderDetailDTO> orderDetailDTOList, LocationFormatMapQuest deliveryAddress) {
        Map<Integer, Integer> productIDsAndQuantities = getOrderedProductsIDsAndQuantitiesMap(orderDetailDTOList);
        List<Integer> locationsIDsWithAvailableStocks = locationRepositoryCustom.findLocationWithProductAndQuantity(productIDsAndQuantities);
        if (!locationsIDsWithAvailableStocks.isEmpty()) {
            List<Stock> stocks = stockRepositoryCustom.findStocksByLocationAndProductIDs(createLocationAndProductIDsList(locationsIDsWithAvailableStocks.get(0), getOrderedProductsIDsList(orderDetailDTOList)));
            return getAndUpdateAvailableStocks(stocks, orderDetailDTOList, stockRepository, stockService);
        }
        throw new ProductsCantBeShipped("Demanded products can't be taken from single location!");

    }

    public List<LocationAndProductIDs> createLocationAndProductIDsList(Integer locationID, List<Integer> orderedProductsIDs) {
        List<LocationAndProductIDs> result = new ArrayList<>();
        for (Integer productID : orderedProductsIDs) {
            result.add(new LocationAndProductIDs(locationID, productID));
        }
        return result;
    }


}
