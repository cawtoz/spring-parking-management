package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.Entry;
import com.github.cawtoz.parking.model.Exit;
import com.github.cawtoz.parking.model.Space;
import com.github.cawtoz.parking.model.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ParkingService {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private EntryService entryService;

    @Autowired
    private ExitService exitService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Entry registerEntry(String vehiclePlate, String vehicleOwner, Space space) {
        if (space.isOccupied()) {
            throw new IllegalStateException("El espacio ya está ocupado.");
        }

        Entry entry = new Entry(vehiclePlate, LocalDateTime.now(), space, vehicleOwner);
        entryService.save(entry);

        spaceService.updateSpaceStatusAsync(space, true);

        notificationService.sendEntryNotification(getAuthenticatedUserEmail(), vehiclePlate, vehicleOwner, space.getParking().getName());

        return entry;
    }

    @Transactional
    public Exit registerExit(Entry entry) {
        if (exitService.exitExistsForEntry(entry)) {
            throw new IllegalStateException("Ya se ha registrado una salida para esta entrada.");
        }

        LocalDateTime exitTime = LocalDateTime.now();
        double amountCharged = calculateCharge(entry.getTimestamp(), exitTime, entry.getSpace().getVehicleType().getHourlyRate());

        Exit exit = new Exit(entry, exitTime, amountCharged);
        exitService.save(exit);

        spaceService.updateSpaceStatusAsync(entry.getSpace(), false);

        notificationService.sendExitNotification(getAuthenticatedUserEmail(), entry.getVehiclePlate(), amountCharged);

        return exit;
    }

    private double calculateCharge(LocalDateTime entryTime, LocalDateTime exitTime, double hourlyRate) {
        long hoursParked = Math.max(Duration.between(entryTime, exitTime).toHours(), 1);
        return hoursParked * hourlyRate;
    }

    private String getAuthenticatedUserEmail() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (customUserDetails != null) {
            return customUserDetails.getEmail();
        }
        return null;
    }


}
