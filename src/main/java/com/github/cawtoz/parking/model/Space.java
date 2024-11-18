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

    @JoinColumn(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", nullable = false)
    private VehicleType vehicleType;

    @Column(nullable = false)
    private boolean isOccupied = false;

    @Column(nullable = false)
    private boolean isActive = true;

    public Space(String name, Parking parking, VehicleType vehicleType) {
        this.parking = parking;
        this.vehicleType = vehicleType;
    }

}
