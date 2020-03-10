package ro.msg.learning.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class StockDTO implements Serializable {
    private Integer location_id;
    private Integer product_id;
    private Integer quantity;


}
