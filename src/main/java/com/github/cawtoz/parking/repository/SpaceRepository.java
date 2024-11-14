package com.github.cawtoz.parking.repository;

import com.github.cawtoz.parking.model.Parking;
import com.github.cawtoz.parking.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {
    List<Space> findByParking(Parking parking);
    long countByParking(Parking parking);
    long countByParkingAndIsOccupied(Parking parking, boolean isOccupied);
    List<Space> findByParkingAndIsOccupied(Parking parking, Boolean isOccupied);
}
