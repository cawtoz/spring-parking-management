package com.github.cawtoz.parking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "entries")
@NoArgsConstructor
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vehicleOwner;

    @Column(nullable = false)
    private String vehiclePlate;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    public Entry(String vehiclePlate, LocalDateTime timestamp, Space space, String vehicleOwner) {
        this.vehiclePlate = vehiclePlate;
        this.timestamp = timestamp;
        this.space = space;
        this.vehicleOwner = vehicleOwner;
    }

}
