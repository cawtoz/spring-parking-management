package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.VehicleType;
import com.github.cawtoz.parking.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleTypeService {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    public VehicleType save(VehicleType vehicleType) {
        return vehicleTypeRepository.save(vehicleType);
    }

    public List<VehicleType> findAll() {
        return vehicleTypeRepository.findAll();
    }

    public VehicleType findById(Long id) {
        return vehicleTypeRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        vehicleTypeRepository.deleteById(id);
    }

    public VehicleType findByName(String name) {
        return vehicleTypeRepository.findAll()
                .stream()
                .filter(vehicleType -> vehicleType.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
