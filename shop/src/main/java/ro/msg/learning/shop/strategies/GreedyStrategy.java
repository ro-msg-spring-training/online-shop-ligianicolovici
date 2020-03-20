package ro.msg.learning.shop.strategies;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.shop.dtos.OrderDetailDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.ProductsCantBeShipped;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.repositories.OrderRepository;
import ro.msg.learning.shop.repositories.StockRepository;
import ro.msg.learning.shop.services.StockService;
import ro.msg.learning.shop.utils.LocationFormatMapQuest;
import ro.msg.learning.shop.utils.MapQuestResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GreedyStrategy implements StrategyChoiceInterface {

    private final StockRepository stockRepository;
    private final StockService stockService;
    private final StockMapper stockMapper;
    private final OrderRepository orderRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @SneakyThrows
    @Override
    public List<StockDTO> implementStrategy(List<OrderDetailDTO> orderDetailDTOList, LocationFormatMapQuest deliveryAddress) {
        List<StockDTO> resultedStocks = new ArrayList<>();
        String resourceUrl = "http://www.mapquestapi.com/directions/v2/routematrix?key=CPdh134iTSAHIoXlDdiP8LFzGrmgZpuA";

        for (OrderDetailDTO demandedProduct : orderDetailDTOList) {
            List<LocationFormatMapQuest> locationsWithSuitableStocks = new ArrayList<>();
            List<Stock> stocksContainingDemandedProduct = stockRepository.findAllByProductId(demandedProduct.getProductId());

            JSONObject postData = new JSONObject();
            JSONArray locationsJSON = new JSONArray();
            locationsJSON.put(deliveryAddress.toString());
            if (stocksContainingDemandedProduct.size() > 1) {
                for (Stock crtStock : stocksContainingDemandedProduct) {
                    if (demandedProduct.getQuantity() <= crtStock.getQuantity()) {
                        LocationFormatMapQuest currentLocation = new LocationFormatMapQuest(crtStock.getLocation().getId(), crtStock.getLocation().getAddress().getCity(), crtStock.getLocation().getAddress().getCountry());
                        locationsWithSuitableStocks.add(currentLocation);
                        locationsJSON.put(currentLocation.toString());
                    }
                }
                postData.put("locations", locationsJSON);
                MapQuestResponse response = restTemplate.postForObject(resourceUrl, postData.toString(), MapQuestResponse.class);
                StockDTO idealStock = findIdealStockByClosestLocation(locationsWithSuitableStocks, response, stocksContainingDemandedProduct, demandedProduct);
                resultedStocks.add(idealStock);
            } else {
                resultedStocks.add(singleLocationCase(stocksContainingDemandedProduct, demandedProduct));
            }
            if (resultedStocks.size() == orderDetailDTOList.size()) {
                return resultedStocks;
            }
        }
        throw new ProductsCantBeShipped("Products not available at the moment!");
    }

    public StockDTO findIdealStockByClosestLocation(List<LocationFormatMapQuest> locationsWithSuitableStocks, MapQuestResponse response, List<Stock> stocksContainingDemandedProduct, OrderDetailDTO demandedProduct) {
        Map<Integer, BigDecimal> locationAndDistance = new HashMap<>();
        StockDTO idealStock;
        int index = 1;
        for (LocationFormatMapQuest stockLocation : locationsWithSuitableStocks) {
            if (response == null)
                throw new AssertionError();
            locationAndDistance.put(stockLocation.getLocationID(), response.getDistance().get(index));
            index++;
        }
        Integer closedStockByLocationID = getClosestLocationById(locationAndDistance);
        idealStock = getIdealStock(closedStockByLocationID, stocksContainingDemandedProduct, demandedProduct);
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

    public StockDTO getIdealStock(Integer closedStockByLocationID, List<Stock> stocksContainingDemandedProduct, OrderDetailDTO demandedProduct) {

        Stock result = null;
        for (Stock stock : stocksContainingDemandedProduct) {
            if (stock.getLocation().getId().equals(closedStockByLocationID)) {
                result = stock;
                break;
            }
        }
        assert result != null;
        StockDTO targetStock = StockDTO.builder()
                .quantity(demandedProduct.getQuantity())
                .locationId(result.getLocation().getId())
                .productId(demandedProduct.getProductId())
                .build();

        stockService.updateStock(result, demandedProduct.getQuantity());

        return targetStock;
    }

    public StockDTO singleLocationCase(List<Stock> stocksContainingDemandedProduct, OrderDetailDTO demandedProduct) {

        StockDTO targetStock = StockDTO.builder()
                .quantity(demandedProduct.getQuantity())
                .locationId(stocksContainingDemandedProduct.get(0).getLocation().getId())
                .productId(demandedProduct.getProductId())
                .build();

        stockService.updateStock(stocksContainingDemandedProduct.get(0), demandedProduct.getQuantity());
        return targetStock;
    }
}
