package ro.msg.learning.shop.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String country;
    private String city;
    private String street;

    @Override
    public String toString() {
        return "Country: " + country + "\n" +
                "City: " + city + "\n" +
                "Street: " + street + "\n";
    }
}
