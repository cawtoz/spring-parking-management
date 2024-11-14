package com.github.cawtoz.parking.repository;

import com.github.cawtoz.parking.model.Entry;
import com.github.cawtoz.parking.model.Exit;
import com.github.cawtoz.parking.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExitRepository extends JpaRepository<Exit, Long> {
    boolean existsByEntry(Entry entry);
    List<Exit> findByEntry(Entry space);
    List<Exit> findByEntry_Space_Parking(Parking parking);
    Exit findFirstByEntryOrderByTimestampDesc(Entry space);
    List<Exit> findByTimestampBetweenAndEntry_Space_Parking(LocalDateTime startOfDay, LocalDateTime endOfDay, Parking parking);
}
