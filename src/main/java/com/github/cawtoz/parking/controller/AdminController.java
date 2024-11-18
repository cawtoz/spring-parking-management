package com.github.cawtoz.parking.controller;

import com.github.cawtoz.parking.model.*;
import com.github.cawtoz.parking.model.security.CustomUserDetails;
import com.github.cawtoz.parking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private EntryService entryService;

    @Autowired
    private ExitService exitService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private VehicleTypeService vehicleTypeService;


    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Parking parking = customUserDetails.getParking();
        long availableSpaces = spaceService.getAvailableSpacesCount(parking);
        long totalSpaces = spaceService.getTotalSpacesCount(parking);
        long totalEntries = entryService.getTotalEntriesToday(parking);
        long totalExits = exitService.getTotalExitsToday(parking);

        model.addAttribute("parking", parking);
        model.addAttribute("availableSpaces", availableSpaces);
        model.addAttribute("totalSpaces", totalSpaces);
        model.addAttribute("totalEntries", totalEntries);
        model.addAttribute("totalExits", totalExits);
        return "admin/dashboard";
    }

    @GetMapping("/espacios")
    public String espacios(Model model, Authentication authentication,
                           @RequestParam(value = "filter", defaultValue = "todos") String filter,
                           @RequestParam(value = "vehicleTypeFilter", defaultValue = "todos") String vehicleTypeFilter) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Parking parking = customUserDetails.getParking();

        List<Space> spaces;

        // Apply the filters
        switch (filter) {
            case "libres":
                spaces = spaceService.getAvailableSpaces(parking);
                break;
            case "ocupados":
                spaces = spaceService.getOccupiedSpaces(parking);
                break;
            default:
                spaces = spaceService.getSpacesByParking(parking);
                break;
        }

        // Filter by vehicle type if selected
        if (!"todos".equals(vehicleTypeFilter)) {
            spaces = spaces.stream()
                    .filter(space -> {
                        String vehicleTypeId = space.getVehicleType().getId().toString();
                        return vehicleTypeId.equals(vehicleTypeFilter);
                    })
                    .collect(Collectors.toList());
        }


        long totalSpaces = spaceService.getTotalSpacesCount(parking);
        long availableSpaces = spaceService.getAvailableSpacesCount(parking);
        long occupiedSpaces = spaceService.getOccupiedSpacesCount(parking);
        List<VehicleType> vehicleTypes = vehicleTypeService.findAll();

        model.addAttribute("totalSpaces", totalSpaces);
        model.addAttribute("availableSpaces", availableSpaces);
        model.addAttribute("occupiedSpaces", occupiedSpaces);
        model.addAttribute("spaces", spaces);
        model.addAttribute("vehicleTypes", vehicleTypes);
        model.addAttribute("filter", filter);
        model.addAttribute("vehicleTypeFilter", vehicleTypeFilter);

        return "admin/espacios";
    }

    @GetMapping("/entradas")
    public String entradas(
            Model model,
            Authentication authentication,
            @RequestParam(value = "entryId", required = false) Long entryId,
            @RequestParam(value = "vehicleType", defaultValue = "todos") String vehicleType,
            @RequestParam(value = "vehiclePlate", required = false) String vehiclePlate,
            @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) LocalDateTime endDate
    ) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Parking parking = customUserDetails.getParking();

        // Obtener todas las entries para el parking
        List<Entry> entries = entryService.getEntriesByParking(parking);

        // Filtrar por fechas
        if (startDate != null && endDate != null) {
            entries = entries.stream()
                    .filter(entry -> !entry.getTimestamp().isBefore(startDate) && !entry.getTimestamp().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        // Filtrar por tipo de vehículo
        if (!"todos".equals(vehicleType)) {
            entries = entries.stream()
                    .filter(entry -> entry.getSpace().getVehicleType().getId().toString().equals(vehicleType))
                    .collect(Collectors.toList());
        }

        // Filtrar por placa de vehículo
        if (vehiclePlate != null && !vehiclePlate.isEmpty()) {
            entries = entries.stream()
                    .filter(entry -> entry.getVehiclePlate().equalsIgnoreCase(vehiclePlate))
                    .collect(Collectors.toList());
        }

        // Filtrar por ID
        if (entryId != null) {
            entries = entries.stream()
                    .filter(entry -> entry.getId().equals(entryId))
                    .collect(Collectors.toList());
        }

        // Agregar datos al modelo
        List<VehicleType> vehicleTypes = vehicleTypeService.findAll();
        model.addAttribute("entries", entries);
        model.addAttribute("vehicleTypes", vehicleTypes);
        model.addAttribute("vehicleType", vehicleType);
        model.addAttribute("vehiclePlate", vehiclePlate);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/entradas";
    }

    @GetMapping("/salidas")
    public String salidas(
            Model model,
            Authentication authentication,
            @RequestParam(value = "vehicleType", defaultValue = "todos") String vehicleType,
            @RequestParam(value = "vehiclePlate", required = false) String vehiclePlate,
            @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) LocalDateTime endDate,
            @RequestParam(value = "exitId", required = false) String exitId,
            @RequestParam(value = "entryId", required = false) String entryId
    ) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Parking parking = customUserDetails.getParking();
        List<Exit> exits = exitService.getExitsByParking(parking);

        // Filtrar por fechas
        if (startDate != null && endDate != null) {
            exits = exits.stream()
                    .filter(exit -> !exit.getTimestamp().isBefore(startDate) && !exit.getTimestamp().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        // Filtrar por tipo de vehículo
        if (!"todos".equals(vehicleType)) {
            exits = exits.stream()
                    .filter(exit -> exit.getEntry().getSpace().getVehicleType().getId().toString().equals(vehicleType))
                    .collect(Collectors.toList());
        }

        // Filtrar por placa de vehículo
        if (vehiclePlate != null && !vehiclePlate.isEmpty()) {
            exits = exits.stream()
                    .filter(exit -> exit.getEntry().getVehiclePlate().equalsIgnoreCase(vehiclePlate))
                    .collect(Collectors.toList());
        }

        // Filtrar por ID
        if (exitId != null && !exitId.isEmpty()) {
            exits = exits.stream()
                    .filter(exit -> exit.getId().toString().equals(exitId))
                    .collect(Collectors.toList());
        }

        // Filtrar por ID de entrada (si es que existe)
        if (entryId != null && !entryId.isEmpty()) {
            exits = exits.stream()
                    .filter(exit -> exit.getEntry().getId().toString().equals(entryId))
                    .collect(Collectors.toList());
        }

        // Agregar datos al modelo
        List<VehicleType> vehicleTypes = vehicleTypeService.findAll();
        model.addAttribute("exits", exits);
        model.addAttribute("vehicleTypes", vehicleTypes);
        model.addAttribute("vehicleType", vehicleType);
        model.addAttribute("vehiclePlate", vehiclePlate);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("entryId", entryId);

        return "admin/salidas";
    }



    @GetMapping("/tarifa")
    public String tarifa() {
        return "admin/tarifa";
    }

    @GetMapping("/asignar")
    public String asignar(Model model, @RequestParam(value = "spaceId") long spaceId) {

        Space space = spaceService.findById(spaceId);
        if (space == null) {
            throw new IllegalArgumentException("El espacio con ID " + spaceId + " no existe.");
        }

        model.addAttribute("space", space);
        return "admin/asignar";

    }


    @PostMapping("/asignar")
    public String asignar(
            @RequestParam(value = "spaceId") long spaceId,
            @RequestParam(value = "vehiclePlate") String vehiclePlate,
            @RequestParam(value = "vehicleOwner") String vehicleOwner
    ) {

        Space space = spaceService.findById(spaceId);

        if (space == null) {
            throw new IllegalArgumentException("El espacio con ID " + spaceId + " no existe.");
        }

        parkingService.registerEntry(vehiclePlate, vehicleOwner, space);
        return "redirect:/admin/espacios";

    }

    @GetMapping("/espacio")
    public String espacio(Model model, @RequestParam(value = "spaceId") long spaceId) {

        Space space = spaceService.findById(spaceId);
        if (space == null) {
            throw new IllegalArgumentException("El espacio con ID " + spaceId + " no existe.");
        }

        Entry entry = entryService.getLastEntryBySpace(space);

        model.addAttribute("space", space);
        model.addAttribute("entry", entry);

        return "admin/espacio";
    }

    @PostMapping("/espacio")
    public String asignar(Space space) {
        spaceService.save(space);
        return "redirect:/admin/espacios";
    }

    @GetMapping("/espacio/eliminar")
    public String asignar(@RequestParam(value = "spaceId") long spaceId) {
        spaceService.deactivateById(spaceId);
        return "redirect:/admin/espacios";
    }

    @GetMapping("/nuevo-espacio")
    public String espacio(Model model, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Parking parking = customUserDetails.getParking();

        Space space = new Space();
        space.setParking(parking);

        model.addAttribute("space", space);
        model.addAttribute("vehicleTypes", vehicleTypeService.findAll());
        return "admin/nuevo-espacio";
    }

    @PostMapping("/salida")
    public String salida(@RequestParam(value = "entryId") long entryId) {
        Entry entry =  entryService.findById(entryId);
        parkingService.registerExit(entry);
        return "redirect:/admin/espacios";
    }

}
