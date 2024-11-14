package com.github.cawtoz.parking.repository;

import com.github.cawtoz.parking.model.Entry;
import com.github.cawtoz.parking.model.Parking;
import com.github.cawtoz.parking.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findBySpace(Space space);
    List<Entry> findBySpaceParking(Parking parking);
    Entry findFirstBySpaceOrderByTimestampDesc(Space space);
    List<Entry> findBySpaceParkingAndTimestampBetween(Parking parking, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
