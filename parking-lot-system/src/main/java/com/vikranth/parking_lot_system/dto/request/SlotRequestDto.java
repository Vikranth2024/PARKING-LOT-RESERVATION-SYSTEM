package com.vikranth.parking_lot_system.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SlotRequestDto {

    @NotBlank(message = "Slot number cannot be empty.")
    private String slotNumber;

    @NotNull(message = "Floor ID cannot be null.")
    @Min(value = 1, message = "Floor ID must be a positive number.")
    private Long floorId;

    @NotNull(message = "Vehicle type ID cannot be null.")
    @Min(value = 1, message = "Vehicle type ID must be a positive number.")
    private Long vehicleTypeId;

    public SlotRequestDto() {
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Long getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(Long vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }
}
