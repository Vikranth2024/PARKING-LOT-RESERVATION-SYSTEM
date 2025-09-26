package com.vikranth.parking_lot_system.repository;

import com.vikranth.parking_lot_system.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.parkingSlot.id = :slotId AND " +
            "r.endTime > :startTime AND r.startTime < :endTime")


    List<Reservation> findOverlappingReservations(@Param("slotId") Long slotId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    // METHOD to find current and future reservations
    List<Reservation> findByEndTimeAfterOrderByStartTimeAsc(LocalDateTime currentTime);

}
