package com.vikranth.parking_lot_system.controller;

import com.vikranth.parking_lot_system.dto.request.ReservationRequestDto;
import com.vikranth.parking_lot_system.dto.response.AvailableSlotDto;
import com.vikranth.parking_lot_system.dto.response.ReservationResponseDto;
import com.vikranth.parking_lot_system.model.ParkingSlot;
import com.vikranth.parking_lot_system.model.Reservation;
import com.vikranth.parking_lot_system.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto createReservation(@Valid @RequestBody ReservationRequestDto requestDto) {
        Reservation newReservation = reservationService.createReservation(
                requestDto.getSlotId(),
                requestDto.getVehicleNumber(),
                requestDto.getStartTime(),
                requestDto.getEndTime()
        );
        return convertToReservationDto(newReservation);
    }

    @GetMapping("/availability")
    public List<AvailableSlotDto> getAvailableSlots(@RequestParam LocalDateTime startTime,
                                                    @RequestParam LocalDateTime endTime) {
        List<ParkingSlot> availableSlots = reservationService.findAvailableSlots(startTime, endTime);
        return availableSlots.stream()
                .map(this::convertToAvailableSlotDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/reservations/{id}")
    public ReservationResponseDto getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return convertToReservationDto(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }

    @GetMapping("/reservations/active")
    public List<ReservationResponseDto> getActiveReservations() {
        return reservationService.getActiveReservations().stream()
                .map(this::convertToReservationDto)
                .collect(Collectors.toList());
    }


    private ReservationResponseDto convertToReservationDto(Reservation reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setReservationId(reservation.getId());
        dto.setSlotNumber(reservation.getParkingSlot().getSlotNumber());
        dto.setFloorNumber(reservation.getParkingSlot().getParkingFloor().getFloorNumber());
        dto.setVehicleNumber(reservation.getVehicleNumber());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setTotalCost(reservation.getTotalCost());
        dto.setStatus("CONFIRMED"); // Or derive from entity if status field exists
        return dto;
    }

    private AvailableSlotDto convertToAvailableSlotDto(ParkingSlot slot) {
        AvailableSlotDto dto = new AvailableSlotDto();
        dto.setSlotId(slot.getId());
        dto.setSlotNumber(slot.getSlotNumber());
        dto.setFloorNumber(slot.getParkingFloor().getFloorNumber());
        dto.setVehicleTypeName(slot.getVehicleType().getName());
        return dto;
    }
}