package com.vikranth.parking_lot_system.controller;

import com.vikranth.parking_lot_system.dto.request.FloorRequestDto;
import com.vikranth.parking_lot_system.dto.request.SlotRequestDto;
import com.vikranth.parking_lot_system.model.ParkingFloor;
import com.vikranth.parking_lot_system.model.ParkingSlot;
import com.vikranth.parking_lot_system.model.SlotStatus;
import com.vikranth.parking_lot_system.service.ParkingLotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ParkingLotAdminController {

    private final ParkingLotService parkingLotService;

    public ParkingLotAdminController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostMapping("/floors")
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingFloor createParkingFloor(@Valid @RequestBody FloorRequestDto requestDto) {
        return parkingLotService.createParkingFloor(requestDto.getFloorNumber());
    }

    @PostMapping("/slots")
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingSlot createParkingSlot(@Valid @RequestBody SlotRequestDto requestDto) {
        return parkingLotService.createParkingSlot(
                requestDto.getFloorId(),
                requestDto.getSlotNumber(),
                requestDto.getVehicleTypeId()
        );
    }


    @GetMapping("/overview")
    public List<ParkingFloor> getDashboardOverview() {
        return parkingLotService.getDashboardOverview();
    }


    @PutMapping("/slots/{id}/status")
    public ParkingSlot updateSlotStatus(@PathVariable Long id, @RequestParam SlotStatus status) {
        return parkingLotService.updateSlotStatus(id, status);
    }

    @DeleteMapping("/slots/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSlot(@PathVariable Long id) {
        parkingLotService.deleteSlot(id);
    }


}
