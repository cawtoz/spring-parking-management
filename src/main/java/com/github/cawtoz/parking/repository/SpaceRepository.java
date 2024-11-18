package com.github.cawtoz.parking.repository;

import com.github.cawtoz.parking.model.Parking;
import com.github.cawtoz.parking.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {
    // Espacios activos en un parking
    List<Space> findByParkingAndIsActive(Parking parking, boolean isActive);

    // Contar espacios activos en un parking
    long countByParkingAndIsActive(Parking parking, boolean isActive);

    // Contar espacios ocupados y activos en un parking
    long countByParkingAndIsOccupiedAndIsActive(Parking parking, boolean isOccupied, boolean isActive);

    // Obtener espacios ocupados y activos en un parking
    List<Space> findByParkingAndIsOccupiedAndIsActive(Parking parking, boolean isOccupied, boolean isActive);
}

