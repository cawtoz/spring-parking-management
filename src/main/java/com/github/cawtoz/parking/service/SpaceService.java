package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.Parking;
import com.github.cawtoz.parking.model.Space;
import com.github.cawtoz.parking.model.VehicleType;
import com.github.cawtoz.parking.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    public Space save(Space space) {
        return spaceRepository.save(space);
    }

    public List<Space> findAll() {
        return spaceRepository.findAll();
    }

    public Space findById(Long id) {
        return spaceRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        spaceRepository.deleteById(id);
    }

    public void deactivateById(Long id) {
        Space space = findById(id);
        if (space != null) {
            space.setActive(false);
            save(space);
        }
    }

    public long getTotalSpacesCount(Parking parking) {
        return spaceRepository.countByParkingAndIsActive(parking, true);
    }

    public long getAvailableSpacesCount(Parking parking) {
        return spaceRepository.countByParkingAndIsOccupiedAndIsActive(parking, false, true);
    }

    public long getOccupiedSpacesCount(Parking parking) {
        return spaceRepository.countByParkingAndIsOccupiedAndIsActive(parking, true, true);
    }

    public List<Space> getSpacesByParking(Parking parking) {
        return spaceRepository.findByParkingAndIsActive(parking, true);
    }

    public List<Space> getAvailableSpaces(Parking parking) {
        return spaceRepository.findByParkingAndIsOccupiedAndIsActive(parking, false, true);
    }

    public List<Space> getOccupiedSpaces(Parking parking) {
        return spaceRepository.findByParkingAndIsOccupiedAndIsActive(parking, true, true);
    }

}
