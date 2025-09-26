package com.vikranth.parking_lot_system.dto.request;

import jakarta.validation.constraints.NotBlank;

public class FloorRequestDto {
    @NotBlank(message = "Floor number cannot be empty.")
    private String floorNumber;

    public FloorRequestDto() {

    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

}
