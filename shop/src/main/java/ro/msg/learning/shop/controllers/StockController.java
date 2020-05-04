package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.services.StockService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocksToCsv")
public class StockController {
    private final StockService stockService;

    @GetMapping(value = "/{locationId}", produces = {"text/csv"})
    public List<StockDTO> getStocksToCSV(@PathVariable("locationId") Integer locationId, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stocks.csv");
        return stockService.exportStocksToCSV(locationId);
    }
}
