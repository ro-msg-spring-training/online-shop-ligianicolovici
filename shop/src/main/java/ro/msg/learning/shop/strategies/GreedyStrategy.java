package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.repositories.*;
import ro.msg.learning.shop.services.StockService;
import ro.msg.learning.shop.utils.LocationAndProductIDs;
import ro.msg.learning.shop.utils.LocationFormatMapQuest;
import ro.msg.learning.shop.utils.MapQuestResponse;

import java.math.BigDecimal;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GreedyStrategy implements StrategyChoiceInterface {

    private final StockRepository stockRepository;
    private final StockService stockService;
    private final StockRepositoryCustom stockRepositoryCustom;
    private final LocationRepositoryCustom locationRepositoryCustom;
    private final LocationRepository locationRepository;
    private final StockMapper stockMapper;
    private final OrderRepository orderRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @SneakyThrows
    @Override
    public List<StockDTO> implementStrategy(List<OrderDetailDTO> orderDetailDTOList, LocationFormatMapQuest deliveryAddress) {
        List<StockDTO> resultedStocks;
        String resourceUrl = "http://www.mapquestapi.com/directions/v2/routematrix?key=CPdh134iTSAHIoXlDdiP8LFzGrmgZpuA";
        Map<Integer, Integer> productIDsAndQuantities = getOrderedProductsIDsAndQuantitiesMap(orderDetailDTOList);
        List<Integer> locationsIDsWithAvailableStocks = locationRepositoryCustom.findLocationWithProductAndQuantity(productIDsAndQuantities);
        if (locationsIDsWithAvailableStocks.size() > 1) {
            List<LocationFormatMapQuest> suitableLocations = new ArrayList<>();
            JSONObject postData = new JSONObject();
            JSONArray locationsJSON = new JSONArray();
            locationsJSON.put(deliveryAddress.toString());
            Optional<Location> crtLocationBasedOnID;
            for (Integer locationID : locationsIDsWithAvailableStocks) {
                crtLocationBasedOnID = locationRepository.findById(locationID);
                if (crtLocationBasedOnID.isPresent()) {
                    LocationFormatMapQuest currentLocation = new LocationFormatMapQuest(crtLocationBasedOnID.get().getId(), crtLocationBasedOnID.get().getAddress().getCity(), crtLocationBasedOnID.get().getAddress().getCountry());
                    suitableLocations.add(currentLocation);
                    locationsJSON.put(currentLocation.toString());
                }
            }
            postData.put("locations", locationsJSON);
            MapQuestResponse response = restTemplate.postForObject(resourceUrl, postData.toString(), MapQuestResponse.class);
            resultedStocks = findIdealStockByClosestLocation(suitableLocations, response, orderDetailDTOList);
            return resultedStocks;
        } else {
            if (!locationsIDsWithAvailableStocks.isEmpty()) {
                resultedStocks = singleLocationCase(locationsIDsWithAvailableStocks.get(0), orderDetailDTOList);
                return resultedStocks;
            }
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

    public List<StockDTO> findIdealStockByClosestLocation(List<LocationFormatMapQuest> locationsWithSuitableStocks, MapQuestResponse response, List<OrderDetailDTO> orderDetailDTOList) {
        Map<Integer, BigDecimal> locationAndDistance = new HashMap<>();
        List<StockDTO> idealStock;
        int index = 1;
        for (LocationFormatMapQuest stockLocation : locationsWithSuitableStocks) {
            if (response == null)
                throw new AssertionError();
            locationAndDistance.put(stockLocation.getLocationID(), response.getDistance().get(index));
            index++;
        }
        Integer closedStockByLocationID = getClosestLocationById(locationAndDistance);
        List<Stock> result;
        result = stockRepositoryCustom.findStocksByLocationAndProductIDs(createLocationAndProductIDsList(closedStockByLocationID, getOrderedProductsIDsList(orderDetailDTOList)));
        idealStock = getAndUpdateAvailableStocks(result, orderDetailDTOList, stockRepository, stockService);
        return idealStock;
    }

    public Integer getClosestLocationById(Map<Integer, BigDecimal> allAvailableStockLocations) {
        Map.Entry<Integer, BigDecimal> result = null;
        for (Map.Entry<Integer, BigDecimal> entry : allAvailableStockLocations.entrySet()) {
            if ((result == null || (entry.getValue().compareTo(result.getValue()) < 0)) && entry.getValue().compareTo(BigDecimal.ZERO) != 0) {
                result = entry;
            }
        }
        assert result != null;
        return result.getKey();
    }


    public List<StockDTO> singleLocationCase(Integer locationID, List<OrderDetailDTO> orderDetailDTOList) {
        List<Stock> result;
        result = stockRepositoryCustom.findStocksByLocationAndProductIDs(createLocationAndProductIDsList(locationID, getOrderedProductsIDsList(orderDetailDTOList)));
        return getAndUpdateAvailableStocks(result, orderDetailDTOList, stockRepository, stockService);
    }


}
