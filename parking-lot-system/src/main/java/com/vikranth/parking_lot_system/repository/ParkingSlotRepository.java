package com.vikranth.parking_lot_system.repository;

import com.vikranth.parking_lot_system.model.ParkingFloor;
import com.vikranth.parking_lot_system.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    @Query("SELECT s FROM ParkingSlot s WHERE s.id NOT IN (" +
            "SELECT r.parkingSlot.id FROM Reservation r WHERE r.endTime > :startTime AND r.startTime < :endTime" +
            ")")
    List<ParkingSlot> findAvailableSlots(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    boolean existsBySlotNumberAndParkingFloor(String slotNumber, ParkingFloor parkingFloor);

}