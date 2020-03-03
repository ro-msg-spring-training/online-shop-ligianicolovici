package ro.msg.learning.shop.services;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import ro.msg.learning.shop.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.converter.CSVConversion;
import ro.msg.learning.shop.dtos.LocationDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.exceptions.LocationNotFoundException;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.repositories.LocationRepository;
import ro.msg.learning.shop.repositories.StockRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@JsonPropertyOrder({ "quantity", "product_id", "location_id" })
public class StockService {
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final StockMapper stockMapper;

    public void updateStock(Integer productID, Integer locationID, Stock stockToUpdate, Integer quantityTaken){
        Integer oldQuantity = stockToUpdate.getQuantity();
        Integer newQuantity= stockToUpdate.getQuantity()-quantityTaken;
        stockToUpdate.setQuantity(newQuantity);
        stockRepository.save(stockToUpdate);
    }
    public List<StockDTO> exportStocksToCSV(Integer locationID) throws IOException {
        List<StockDTO> resultedStocks = new ArrayList<>();
        Optional<Location> requestedLocation = locationRepository.findById(locationID);
        if(requestedLocation.isPresent()){
            resultedStocks =  stockMapper.stockListToStockListDTO(stockRepository.findAllByLocation_Id(locationID));
            return  resultedStocks;

        }else throw  new LocationNotFoundException("Location does not exist!");
    }


}
