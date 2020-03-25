package ro.msg.learning.shop.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationAndProductIDs {
    private Integer locationID;
    private Integer productID;
}
