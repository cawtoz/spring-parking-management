package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.VehicleType;
import com.github.cawtoz.parking.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehicleTypeService {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Transactional
    @CacheEvict(value = {"vehicleTypes", "vehicleType"}, allEntries = true)
    public VehicleType save(VehicleType vehicleType) {
        return vehicleTypeRepository.save(vehicleType);
    }

    @Cacheable(value = "vehicleTypes")
    public List<VehicleType> findAll() {
        return vehicleTypeRepository.findAll();
    }

    @Cacheable(value = "vehicleType", key = "#id")
    public VehicleType findById(Long id) {
        return vehicleTypeRepository.findById(id).orElse(null);
    }

    @Transactional
    @Async
    @CacheEvict(value = {"vehicleTypes", "vehicleType"}, allEntries = true)
    public void deleteById(Long id) {
        vehicleTypeRepository.deleteById(id);
    }

    @Cacheable(value = "vehicleType", key = "#name")
    public VehicleType findByName(String name) {
        return vehicleTypeRepository.findAll()
                .stream()
                .filter(vehicleType -> vehicleType.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
