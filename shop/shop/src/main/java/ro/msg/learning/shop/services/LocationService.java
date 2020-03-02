package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.LocationDTO;
import ro.msg.learning.shop.entities.Location;
import ro.msg.learning.shop.mappers.LocationMapper;
import ro.msg.learning.shop.repositories.LocationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public List<LocationDTO> getAllLocations(){
        List<LocationDTO> allLocationToDTO = new ArrayList<>();
        List<Location>locations = locationRepository.findAll();
        for(Location l:locations){
            allLocationToDTO.add(locationMapper.locationToLocationDTO(l));
        }
        return allLocationToDTO;
    }
}
