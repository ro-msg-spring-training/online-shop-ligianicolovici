package ro.msg.learning.shop.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CustomerDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}
