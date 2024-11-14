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

    public long getTotalSpacesCount(Parking parking) {
        return spaceRepository.countByParking(parking);
    }

    public long getAvailableSpacesCount(Parking parking) {
        return spaceRepository.countByParkingAndIsOccupied(parking, false);
    }

    public long getOccupiedSpacesCount(Parking parking) {
        return spaceRepository.countByParkingAndIsOccupied(parking, true);
    }

    public List<Space> getSpacesByParking(Parking parking) {
        return spaceRepository.findByParking(parking);
    }

    public List<Space> getAvailableSpaces(Parking parking) {
        return spaceRepository.findByParkingAndIsOccupied(parking, false);
    }

    public List<Space> getOccupiedSpaces(Parking parking) {
        return spaceRepository.findByParkingAndIsOccupied(parking, true);
    }

}
