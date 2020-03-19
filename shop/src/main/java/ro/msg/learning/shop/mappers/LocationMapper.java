package ro.msg.learning.shop.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.LocationDTO;
import ro.msg.learning.shop.entities.Location;

@Component
@RequiredArgsConstructor
public class LocationMapper {
    public LocationDTO locationToLocationDTO(Location location) {
        return LocationDTO.builder()
                .addressCity(location.getAddressCity())
                .addressCountry(location.getAddressCountry())
                .addressStreet(location.getAddressStreet())
                .name(location.getName())
                .build();

    }

    public Location locationDTOToLocation(LocationDTO locationDTO) {
        return Location.builder()
                .addressCity(locationDTO.getAddressCity())
                .addressCountry(locationDTO.getAddressCountry())
                .addressStreet(locationDTO.getAddressStreet())
                .name(locationDTO.getName())
                .build();
    }
}
