package ro.msg.learning.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDTO {
    private Integer userID;
    private List<OrderDetailDTO> orderedProducts;
    private String addressCity;
    private String addressStreet;
    private String addressCountry;
}

