package com.vikranth.parking_lot_system.service;

import com.vikranth.parking_lot_system.model.VehicleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class PricingServiceTest {

    private PricingService pricingService;

    @BeforeEach
    void setUp() {

        pricingService = new PricingService();
    }

    @Test
    void testCalculatePrice_WhenGivenFullHours_ShouldReturnCorrectPrice() {

        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 12, 0); // Exactly 2 hours
        VehicleType fourWheeler = new VehicleType("4 wheeler", new BigDecimal("30.00"));
        BigDecimal expectedPrice = new BigDecimal("60.00"); // 2 hours * 30.00

        // Act (Call the method we are testing)
        BigDecimal actualPrice = pricingService.calculatePrice(startTime, endTime, fourWheeler);

        // Assert (Check if the result is correct)
        Assertions.assertEquals(0, expectedPrice.compareTo(actualPrice));
    }

    @Test
    void testCalculatePrice_WhenGivenPartialHour_ShouldRoundUpAndReturnCorrectPrice() {

        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 11, 15); // 1 hour and 15 minutes
        VehicleType twoWheeler = new VehicleType("2 wheeler", new BigDecimal("20.00"));

        BigDecimal expectedPrice = new BigDecimal("40.00"); // 2 hours * 20.00

        BigDecimal actualPrice = pricingService.calculatePrice(startTime, endTime, twoWheeler);

        Assertions.assertEquals(0, expectedPrice.compareTo(actualPrice));
    }
}

