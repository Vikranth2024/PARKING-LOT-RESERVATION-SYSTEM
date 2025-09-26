package com.vikranth.parking_lot_system.repository;

import com.vikranth.parking_lot_system.model.ParkingFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingFloorRepository extends JpaRepository<ParkingFloor, Long> {

}
