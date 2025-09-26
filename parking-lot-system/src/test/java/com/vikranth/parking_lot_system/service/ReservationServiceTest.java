package com.vikranth.parking_lot_system.service;

import com.vikranth.parking_lot_system.exception.ResourceNotFoundException;
import com.vikranth.parking_lot_system.exception.SlotNotAvailableException;
import com.vikranth.parking_lot_system.model.ParkingFloor;
import com.vikranth.parking_lot_system.model.ParkingSlot;
import com.vikranth.parking_lot_system.model.Reservation;
import com.vikranth.parking_lot_system.model.SlotStatus;
import com.vikranth.parking_lot_system.model.VehicleType;
import com.vikranth.parking_lot_system.repository.ParkingSlotRepository;
import com.vikranth.parking_lot_system.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private ReservationService reservationService;

    private ParkingSlot availableSlot;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ParkingFloor floor = new ParkingFloor("G");
        VehicleType vehicleType = new VehicleType("4 wheeler", new BigDecimal("30.00"));
        availableSlot = new ParkingSlot();
        availableSlot.setId(1L);
        availableSlot.setSlotStatus(SlotStatus.AVAILABLE);
        availableSlot.setParkingFloor(floor);
        availableSlot.setVehicleType(vehicleType);

        startTime = LocalDateTime.now().plusHours(1);
        endTime = LocalDateTime.now().plusHours(3);
    }

    @Test
    void createReservation_success() {
        when(parkingSlotRepository.findById(1L)).thenReturn(Optional.of(availableSlot));
        when(reservationRepository.findOverlappingReservations(1L, startTime, endTime)).thenReturn(Collections.emptyList());
        when(pricingService.calculatePrice(startTime, endTime, availableSlot.getVehicleType())).thenReturn(new BigDecimal("60.00"));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation reservation = reservationService.createReservation(1L, "KA01AB1234", startTime, endTime);

        assertNotNull(reservation);
        assertEquals(SlotStatus.RESERVED, availableSlot.getSlotStatus());
        verify(parkingSlotRepository, times(1)).save(availableSlot);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_throwsException_whenSlotNotAvailable() {
        availableSlot.setSlotStatus(SlotStatus.RESERVED);
        when(parkingSlotRepository.findById(1L)).thenReturn(Optional.of(availableSlot));

        assertThrows(SlotNotAvailableException.class, () -> {
            reservationService.createReservation(1L, "KA01AB1234", startTime, endTime);
        });
    }

    @Test
    void createReservation_throwsException_whenOverlapping() {
        when(parkingSlotRepository.findById(1L)).thenReturn(Optional.of(availableSlot));
        when(reservationRepository.findOverlappingReservations(1L, startTime, endTime)).thenReturn(List.of(new Reservation()));

        assertThrows(SlotNotAvailableException.class, () -> {
            reservationService.createReservation(1L, "KA01AB1234", startTime, endTime);
        });
    }

    @Test
    void cancelReservation_success() {
        availableSlot.setSlotStatus(SlotStatus.RESERVED);
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setParkingSlot(availableSlot);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        doNothing().when(reservationRepository).delete(reservation);

        reservationService.cancelReservation(1L);

        assertEquals(SlotStatus.AVAILABLE, availableSlot.getSlotStatus());
        verify(parkingSlotRepository, times(1)).save(availableSlot);
        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    void getReservationById_throwsException_whenNotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            reservationService.getReservationById(99L);
        });
    }
}
