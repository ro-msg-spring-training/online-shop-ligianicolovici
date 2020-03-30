package ro.msg.learning.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.dtos.CustomerDTO;
import ro.msg.learning.shop.services.CustomerService;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping(value = "/{username}", produces = {"application/json"})
    @ResponseBody
    public CustomerDTO findCustomerByUsername(@PathVariable String username) {
        return customerService.getCustomerByUsername(username);
    }
}
