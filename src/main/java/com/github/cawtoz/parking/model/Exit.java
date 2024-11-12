package com.github.cawtoz.parking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "exits")
@NoArgsConstructor
public class Exit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id", nullable = false)
    private Entry entry;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Double amountCharged;

    public Exit(Entry entry, LocalDateTime timestamp, Double amountCharged) {
        this.entry = entry;
        this.timestamp = timestamp;
        this.amountCharged = amountCharged;
    }

}
