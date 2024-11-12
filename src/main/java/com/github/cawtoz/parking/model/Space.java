package com.github.cawtoz.parking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "spaces")
@NoArgsConstructor
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    private Parking parking;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", nullable = false)
    private VehicleType vehicleType;

    @Column(nullable = false)
    private boolean isOccupied = false;

    public Space(Parking parking, VehicleType vehicleType) {
        this.parking = parking;
        this.vehicleType = vehicleType;
    }

}
