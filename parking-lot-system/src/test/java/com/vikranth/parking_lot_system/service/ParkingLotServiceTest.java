package com.vikranth.parking_lot_system.service;

import com.vikranth.parking_lot_system.exception.ResourceNotFoundException;
import com.vikranth.parking_lot_system.model.ParkingFloor;
import com.vikranth.parking_lot_system.model.ParkingSlot;
import com.vikranth.parking_lot_system.model.SlotStatus;
import com.vikranth.parking_lot_system.model.VehicleType;
import com.vikranth.parking_lot_system.repository.ParkingFloorRepository;
import com.vikranth.parking_lot_system.repository.ParkingSlotRepository;
import com.vikranth.parking_lot_system.repository.VehicleTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ParkingLotServiceTest {

    @Mock
    private ParkingFloorRepository parkingFloorRepository;

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @InjectMocks
    private ParkingLotService parkingLotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createParkingFloor_success() {
        ParkingFloor floor = new ParkingFloor("G");
        floor.setId(1L);
        when(parkingFloorRepository.save(any(ParkingFloor.class))).thenReturn(floor);

        ParkingFloor createdFloor = parkingLotService.createParkingFloor("G");

        assertNotNull(createdFloor);
        assertEquals("G", createdFloor.getFloorNumber());
        verify(parkingFloorRepository, times(1)).save(any(ParkingFloor.class));
    }

    @Test
    void createParkingSlot_success() {
        ParkingFloor floor = new ParkingFloor("G");
        floor.setId(1L);
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(1L);
        ParkingSlot slot = new ParkingSlot();
        slot.setId(101L);

        when(parkingFloorRepository.findById(1L)).thenReturn(Optional.of(floor));
        when(vehicleTypeRepository.findById(1L)).thenReturn(Optional.of(vehicleType));
        when(parkingSlotRepository.existsBySlotNumberAndParkingFloor("G-01", floor)).thenReturn(false);
        when(parkingSlotRepository.save(any(ParkingSlot.class))).thenReturn(slot);

        ParkingSlot createdSlot = parkingLotService.createParkingSlot(1L, "G-01", 1L);

        assertNotNull(createdSlot);
        verify(parkingSlotRepository, times(1)).save(any(ParkingSlot.class));
    }

    @Test
    void createParkingSlot_throwsException_whenFloorNotFound() {
        when(parkingFloorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            parkingLotService.createParkingSlot(99L, "G-01", 1L);
        });
    }

    @Test
    void deleteSlot_success() {
        when(parkingSlotRepository.existsById(1L)).thenReturn(true);
        doNothing().when(parkingSlotRepository).deleteById(1L);

        parkingLotService.deleteSlot(1L);

        verify(parkingSlotRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSlot_throwsException_whenSlotNotFound() {
        when(parkingSlotRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            parkingLotService.deleteSlot(99L);
        });
    }

    @Test
    void updateSlotStatus_success() {
        ParkingSlot slot = new ParkingSlot();
        slot.setId(1L);
        slot.setSlotStatus(SlotStatus.AVAILABLE);

        when(parkingSlotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(parkingSlotRepository.save(any(ParkingSlot.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ParkingSlot updatedSlot = parkingLotService.updateSlotStatus(1L, SlotStatus.MAINTENANCE);

        assertNotNull(updatedSlot);
        assertEquals(SlotStatus.MAINTENANCE, updatedSlot.getSlotStatus());
        verify(parkingSlotRepository, times(1)).save(slot);
    }
}