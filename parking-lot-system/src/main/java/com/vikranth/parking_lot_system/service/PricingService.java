package com.vikranth.parking_lot_system.service;

import com.vikranth.parking_lot_system.model.VehicleType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PricingService {


    public BigDecimal calculatePrice(LocalDateTime startTime, LocalDateTime endTime, VehicleType vehicleType) {

        Duration duration = Duration.between(startTime, endTime);
        long minutes = duration.toMinutes();

        long hours = (minutes + 59) / 60;

        BigDecimal hourlyRate = vehicleType.getHourlyRate();

        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }
}


