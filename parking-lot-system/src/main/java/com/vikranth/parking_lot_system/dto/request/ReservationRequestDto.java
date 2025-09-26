package com.vikranth.parking_lot_system.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ReservationRequestDto {

    @NotNull(message = "Slot ID cannot be null.")
    @Min(value = 1, message = "Slot ID must be a positive number.")
    private Long slotId;

    @NotBlank(message = "Vehicle number cannot be empty.")
    @Pattern(regexp = "[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}", message = "Invalid vehicle number format. Expected format is XX00XX0000.")
    private String vehicleNumber;

    @NotNull(message = "Start time cannot be null.")
    @Future(message = "Start time must be in the future.")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null.")
    @Future(message = "End time must be in the future.") // can book slot only in future
    private LocalDateTime endTime;

    public ReservationRequestDto() {
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
