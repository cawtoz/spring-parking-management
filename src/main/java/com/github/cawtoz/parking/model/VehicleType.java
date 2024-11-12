package com.github.cawtoz.parking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "vehicle_types")
@NoArgsConstructor
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double hourlyRate;

    public VehicleType(String name, double hourlyRate) {
        this.name = name;
        this.hourlyRate = hourlyRate;
    }

}
