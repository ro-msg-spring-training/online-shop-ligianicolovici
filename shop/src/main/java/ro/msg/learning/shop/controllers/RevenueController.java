package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.RevenueDTO;
import ro.msg.learning.shop.services.RevenueService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping(value = "/{givenDate}", produces = {"application/json"})
    @ResponseBody
    public List<RevenueDTO> getRevenueListForGivenDate(@PathVariable String givenDate) {
        LocalDate date = LocalDate.parse(givenDate);
        return revenueService.getAllRevenueForGivenDate(date);
    }

}
