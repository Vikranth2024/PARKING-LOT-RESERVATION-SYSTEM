package com.vikranth.parking_lot_system.service;

import com.vikranth.parking_lot_system.exception.ResourceNotFoundException;
import com.vikranth.parking_lot_system.exception.SlotNotAvailableException;
import com.vikranth.parking_lot_system.model.ParkingSlot;
import com.vikranth.parking_lot_system.model.Reservation;
import com.vikranth.parking_lot_system.model.SlotStatus;
import com.vikranth.parking_lot_system.repository.ParkingSlotRepository;
import com.vikranth.parking_lot_system.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final PricingService pricingService;

    public ReservationService(ReservationRepository reservationRepository,
                              ParkingSlotRepository parkingSlotRepository,
                              PricingService pricingService) {
        this.reservationRepository = reservationRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.pricingService = pricingService;
    }

    @Transactional
    public Reservation createReservation(Long slotId, String vehicleNumber, LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        if (Duration.between(startTime, endTime).toHours() > 24) {
            throw new IllegalArgumentException("Reservation cannot exceed 24 hours.");
        }

        ParkingSlot slot = parkingSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found with ID: " + slotId));

        if (slot.getSlotStatus() != SlotStatus.AVAILABLE) {
            throw new SlotNotAvailableException("Parking slot is not available.");
        }

        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(slotId, startTime, endTime);
        if (!overlapping.isEmpty()) {
            throw new SlotNotAvailableException("Slot has an overlapping reservation. It might have been booked by someone else.");
        }

        BigDecimal totalCost = pricingService.calculatePrice(startTime, endTime, slot.getVehicleType());

        // Step 5: Create and save the reservation
        Reservation reservation = new Reservation();
        reservation.setParkingSlot(slot);
        reservation.setVehicleNumber(vehicleNumber);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setTotalCost(totalCost);

        slot.setSlotStatus(SlotStatus.RESERVED);
        parkingSlotRepository.save(slot);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + reservationId));

        ParkingSlot slot = reservation.getParkingSlot();
        slot.setSlotStatus(SlotStatus.AVAILABLE);
        parkingSlotRepository.save(slot);

        reservationRepository.delete(reservation);
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + reservationId)); // UPDATED
    }

    public List<ParkingSlot> findAvailableSlots(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        return parkingSlotRepository.findAvailableSlots(startTime, endTime);
    }

    public List<Reservation> getActiveReservations() {
        return reservationRepository.findByEndTimeAfterOrderByStartTimeAsc(LocalDateTime.now());
    }

}
