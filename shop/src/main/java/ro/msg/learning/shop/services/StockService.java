package ro.msg.learning.shop.services;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@JsonPropertyOrder({"quantity", "product_id", "location_id"})
public class StockService {
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final StockMapper stockMapper;

    public void updateStock(Stock stockToUpdate, Integer quantityTaken) {
        Integer newQuantity = stockToUpdate.getQuantity() - quantityTaken;
        stockToUpdate.setQuantity(newQuantity);
        stockRepository.save(stockToUpdate);
    }

    public List<StockDTO> exportStocksToCSV(Integer locationID) {
        List<StockDTO> resultedStocks;
        Optional<Location> requestedLocation = locationRepository.findById(locationID);
        if (requestedLocation.isPresent()) {
            resultedStocks = stockMapper.stockListToStockListDTO(stockRepository.findAllByLocationId(locationID));
            return resultedStocks;

        } else throw new LocationNotFoundException("Location does not exist!");
    }


}
