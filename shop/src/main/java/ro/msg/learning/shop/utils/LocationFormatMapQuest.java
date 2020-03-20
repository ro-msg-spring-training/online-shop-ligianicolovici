package ro.msg.learning.shop.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationFormatMapQuest {

    private Integer locationID;
    private String cityAndCountry;

    public LocationFormatMapQuest(Integer locationID, String city, String country) {
        this.locationID = locationID;
        this.cityAndCountry = city + "," + country;
    }

    @Override
    public String toString() {
        return cityAndCountry;
    }
}

