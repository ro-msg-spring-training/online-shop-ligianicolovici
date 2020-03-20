package ro.msg.learning.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.utils.Address;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTOScheduler {
    private Integer orderID;
    private Integer userID;
    private Set<Location> locationSet;
    private List<OrderDetailDTO> orderedProducts;
    private Address address;
}
