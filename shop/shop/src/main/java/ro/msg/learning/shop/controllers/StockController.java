package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.dtos.StockDTO;
import ro.msg.learning.shop.services.StockService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping(value = "/{location_id}/stocksToCsv", produces ={"text/csv"})
    public List<StockDTO> getStocksToCSV(@PathVariable ("location_id") Integer location_id, HttpServletResponse response) throws IOException {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stocks.csv");
        return stockService.exportStocksToCSV(location_id);
    }
}
