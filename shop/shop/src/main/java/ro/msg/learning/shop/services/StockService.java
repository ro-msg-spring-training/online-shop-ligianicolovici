package ro.msg.learning.shop.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.LocationDTO;
import ro.msg.learning.shop.dtos.ProductDTO;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.entities.Product;
import ro.msg.learning.shop.entities.Stock;
import ro.msg.learning.shop.mappers.LocationMapper;
import ro.msg.learning.shop.mappers.ProductMapper;
import ro.msg.learning.shop.mappers.StockMapper;
import ro.msg.learning.shop.repositories.StockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;



    public void updateStock(Integer productID, Integer locationID, Stock stockToUpdate, Integer quantityTaken){
        Integer oldQuantity = stockToUpdate.getQuantity();
        Integer newQuantity= stockToUpdate.getQuantity()-quantityTaken;
        stockToUpdate.setQuantity(newQuantity);
        stockRepository.save(stockToUpdate);
    }

}
