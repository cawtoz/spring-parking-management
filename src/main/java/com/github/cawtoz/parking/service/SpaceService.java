package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.Parking;
import com.github.cawtoz.parking.model.Space;
import com.github.cawtoz.parking.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    @Transactional
    @CacheEvict(value = {"availableSpaces", "occupiedSpaces", "spacesByParking"}, key = "#space.parking.id")
    public Space save(Space space) {
        return spaceRepository.save(space);
    }

    public List<Space> findAll() {
        return spaceRepository.findAll();
    }

    public Space findById(Long id) {
        return spaceRepository.findById(id).orElse(null);
    }

    @Transactional
    @Async
    @CacheEvict(value = {"availableSpaces", "occupiedSpaces", "spacesByParking"}, allEntries = true)
    public void deleteById(Long id) {
        spaceRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = {"availableSpaces", "occupiedSpaces", "spacesByParking"}, key = "#id")
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

    @Cacheable(value = "availableSpaces", key = "#parking.id")
    public long getAvailableSpacesCount(Parking parking) {
        return spaceRepository.countByParkingAndIsOccupiedAndIsActive(parking, false, true);
    }

    @Cacheable(value = "availableSpaces", key = "#parking.id")
    public long getOccupiedSpacesCount(Parking parking) {
        return spaceRepository.countByParkingAndIsOccupiedAndIsActive(parking, true, true);
    }

    @Cacheable(value = "spacesByParking", key = "#parking.id")
    public List<Space> getSpacesByParking(Parking parking) {
        return spaceRepository.findByParkingAndIsActive(parking, true);
    }

    @Cacheable(value = "availableSpaces", key = "#parking.id")
    public List<Space> getAvailableSpaces(Parking parking) {
        return spaceRepository.findByParkingAndIsOccupiedAndIsActive(parking, false, true);
    }

    @Cacheable(value = "occupiedSpaces", key = "#parking.id")
    public List<Space> getOccupiedSpaces(Parking parking) {
        return spaceRepository.findByParkingAndIsOccupiedAndIsActive(parking, true, true);
    }

    @Transactional
    @Async
    public void updateSpaceStatusAsync(Space space, boolean occupied) {
        space.setOccupied(occupied);
        save(space);
    }

}
