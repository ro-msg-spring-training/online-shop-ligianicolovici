package ro.msg.learning.shop.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MapQuestResponse {
    private boolean allToAll;
    private List<BigDecimal> distance;

}
