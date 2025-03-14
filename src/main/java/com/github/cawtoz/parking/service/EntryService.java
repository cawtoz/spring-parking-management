package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.Entry;
import com.github.cawtoz.parking.model.Parking;
import com.github.cawtoz.parking.model.Space;
import com.github.cawtoz.parking.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EntryService {

    @Autowired
    private EntryRepository entryRepository;

    @Transactional
    @CacheEvict(value = {"entriesToday", "entriesByParking"}, key = "#entry.space.parking.id")
    public Entry save(Entry entry) {
        return entryRepository.save(entry);
    }

    public List<Entry> findAll() {
        return entryRepository.findAll();
    }

    public Entry findById(Long id) {
        return entryRepository.findById(id).orElse(null);
    }

    @Transactional
    @Async
    @CacheEvict(value = {"entriesToday", "entriesByParking"}, allEntries = true)
    public void deleteById(Long id) {
        entryRepository.deleteById(id);
    }

    public List<Entry> getEntriesBySpace(Space space) {
        return entryRepository.findBySpace(space);
    }

    @Cacheable(value = "entriesByParking", key = "#parking.id")
    public List<Entry> getEntriesByParking(Parking parking) {
        return entryRepository.findBySpaceParking(parking);
    }

    public Entry getLastEntryBySpace(Space space) {
        return entryRepository.findFirstBySpaceOrderByTimestampDesc(space);
    }

    @Cacheable(value = "entriesByTimeframe", key = "#parking.id + '_' + #startOfDay.toString() + '_' + #endOfDay.toString()")
    public List<Entry> getEntriesWithinTimeframeAndParking(Parking parking, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return entryRepository.findBySpaceParkingAndTimestampBetween(parking, startOfDay, endOfDay);
    }

    @Cacheable(value = "entriesToday", key = "#parking.id")
    public long getTotalEntriesToday(Parking parking) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        List<Entry> entriesToday = getEntriesWithinTimeframeAndParking(parking, startOfDay, endOfDay);
        return entriesToday.size();
    }

}
