package com.github.cawtoz.parking.service;

import com.github.cawtoz.parking.model.Entry;
import com.github.cawtoz.parking.model.Exit;
import com.github.cawtoz.parking.model.Space;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public Entry registerEntry(String vehiclePlate, String vehicleOwner, Space space) {

        // Verificar si el espacio ya está ocupado
        if (space.isOccupied()) {
            throw new IllegalStateException("El espacio ya está ocupado.");
        }

        // Crear una nueva entrada
        Entry entry = new Entry(vehiclePlate, LocalDateTime.now(), space, vehicleOwner);
        entryService.save(entry);

        // Marcar el espacio como ocupado
        space.setOccupied(true);
        spaceService.save(space);

        return entry;
    }

    @Transactional
    public Exit registerExit(Entry entry) {
        // Verificar si ya existe una salida para esta entrada
        if (exitService.exitExistsForEntry(entry)) {
            throw new IllegalStateException("Ya se ha registrado una salida para esta entrada.");
        }

        // Calcular el tiempo transcurrido y el monto a cobrar
        LocalDateTime exitTime = LocalDateTime.now();
        double hourlyRate = entry.getSpace().getVehicleType().getHourlyRate();

        // Calcular horas de estacionamiento, con mínimo de 1 hora si es menor
        long hoursParked = Duration.between(entry.getTimestamp(), exitTime).toHours();
        if (hoursParked == 0) {
            hoursParked = 1;  // Cobrar al menos 1 hora si no se llegó a 1 hora
        }

        double amountCharged = hoursParked * hourlyRate;

        // Crear la nueva salida
        Exit exit = new Exit(entry, exitTime, amountCharged);
        exitService.save(exit);

        // Marcar el espacio como libre
        Space space = entry.getSpace();
        space.setOccupied(false);
        spaceService.save(space);

        return exit;
    }

}
