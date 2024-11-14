package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.Exit;
import com.github.cawtoz.parking.model.Entry;
import com.github.cawtoz.parking.model.Parking;
import com.github.cawtoz.parking.repository.ExitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExitService {

    @Autowired
    private ExitRepository exitRepository;

    public Exit save(Exit exit) {
        return exitRepository.save(exit);
    }

    public List<Exit> findAll() {
        return exitRepository.findAll();
    }

    public Exit findById(Long id) {
        return exitRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        exitRepository.deleteById(id);
    }

    public List<Exit> getExitsByEntry(Entry entry) {
        return exitRepository.findByEntry(entry);
    }

    public List<Exit> getExitsByParking(Parking parking) {
        return exitRepository.findByEntry_Space_Parking(parking);
    }

    public boolean exitExistsForEntry(Entry entry) {
        return exitRepository.existsByEntry(entry);
    }

    public Exit getLastExitByEntry(Entry entry) {
        return exitRepository.findFirstByEntryOrderByTimestampDesc(entry);
    }

    public List<Exit> getExitsWithinTimeframeAndParking(Parking parking, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return exitRepository.findByTimestampBetweenAndEntry_Space_Parking(startOfDay, endOfDay, parking);
    }

    public long getTotalExitsToday(Parking parking) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        List<Exit> exitsToday = getExitsWithinTimeframeAndParking(parking, startOfDay, endOfDay);
        return exitsToday.size();
    }

}
