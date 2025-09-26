//package com.vikranth.parking_lot_system.service;
//
//import com.vikranth.parking_lot_system.exception.ResourceNotFoundException;
//import com.vikranth.parking_lot_system.model.ParkingFloor;
//import com.vikranth.parking_lot_system.model.ParkingSlot;
//import com.vikranth.parking_lot_system.model.SlotStatus;
//import com.vikranth.parking_lot_system.model.VehicleType;
//import com.vikranth.parking_lot_system.repository.ParkingFloorRepository;
//import com.vikranth.parking_lot_system.repository.ParkingSlotRepository;
//import com.vikranth.parking_lot_system.repository.VehicleTypeRepository;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class ParkingLotService {
//
//    private final ParkingFloorRepository parkingFloorRepository;
//    private final ParkingSlotRepository parkingSlotRepository;
//    private final VehicleTypeRepository vehicleTypeRepository;
//
//    public ParkingLotService(ParkingFloorRepository parkingFloorRepository,
//                             ParkingSlotRepository parkingSlotRepository,
//                             VehicleTypeRepository vehicleTypeRepository) {
//        this.parkingFloorRepository = parkingFloorRepository;
//        this.parkingSlotRepository = parkingSlotRepository;
//        this.vehicleTypeRepository = vehicleTypeRepository;
//    }
//
//    public ParkingFloor createParkingFloor(String floorNumber) {
//        ParkingFloor parkingFloor = new ParkingFloor(floorNumber);
//        return parkingFloorRepository.save(parkingFloor);
//    }
//
//    public ParkingSlot createParkingSlot(Long floorId, String slotNumber, Long vehicleTypeId) {
//
//        ParkingFloor parkingFloor = parkingFloorRepository.findById(floorId)
//                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with ID: " + floorId));
//
//        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Vehicle Type not found with ID: " + vehicleTypeId));
//
//        ParkingSlot parkingSlot = new ParkingSlot();
//        parkingSlot.setSlotNumber(slotNumber);
//        parkingSlot.setParkingFloor(parkingFloor);
//        parkingSlot.setVehicleType(vehicleType);
//        parkingSlot.setSlotStatus(SlotStatus.AVAILABLE);
//
//        return parkingSlotRepository.save(parkingSlot);
//    }
//
//    public List<ParkingFloor> getDashboardOverview() {
//        return parkingFloorRepository.findAll();
//    }
//
//    public void deleteSlot(Long slotId) {
//
//        if (!parkingSlotRepository.existsById(slotId)) {
//            throw new ResourceNotFoundException("Parking slot not found with ID: " + slotId);
//        }
//        parkingSlotRepository.deleteById(slotId);
//    }
//
//}



package com.vikranth.parking_lot_system.service;

import com.vikranth.parking_lot_system.exception.ResourceNotFoundException;
import com.vikranth.parking_lot_system.model.ParkingFloor;
import com.vikranth.parking_lot_system.model.ParkingSlot;
import com.vikranth.parking_lot_system.model.SlotStatus;
import com.vikranth.parking_lot_system.model.VehicleType;
import com.vikranth.parking_lot_system.repository.ParkingFloorRepository;
import com.vikranth.parking_lot_system.repository.ParkingSlotRepository;
import com.vikranth.parking_lot_system.repository.VehicleTypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParkingLotService {

    private final ParkingFloorRepository parkingFloorRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

    public ParkingLotService(ParkingFloorRepository parkingFloorRepository,
                             ParkingSlotRepository parkingSlotRepository,
                             VehicleTypeRepository vehicleTypeRepository) {
        this.parkingFloorRepository = parkingFloorRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    public ParkingFloor createParkingFloor(String floorNumber) {
        ParkingFloor parkingFloor = new ParkingFloor(floorNumber);
        return parkingFloorRepository.save(parkingFloor);
    }


//    public ParkingSlot createParkingSlot(Long floorId, String slotNumber, Long vehicleTypeId) {
//        ParkingFloor parkingFloor = parkingFloorRepository.findById(floorId)
//                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with ID: " + floorId));
//
//        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Vehicle Type not found with ID: " + vehicleTypeId));
//
//        String expectedPrefix = parkingFloor.getFloorNumber() + "-";
//        if (!slotNumber.startsWith(expectedPrefix)) {
//
//            throw new IllegalArgumentException(
//                    "Slot name '" + slotNumber + "' does not match the selected floor '" + parkingFloor.getFloorNumber() + "'. Please use a name like '" + expectedPrefix + "01'."
//            );
//        }
//
//        ParkingSlot parkingSlot = new ParkingSlot();
//        parkingSlot.setSlotNumber(slotNumber);
//        parkingSlot.setParkingFloor(parkingFloor);
//        parkingSlot.setVehicleType(vehicleType);
//        parkingSlot.setSlotStatus(SlotStatus.AVAILABLE);
//
//        return parkingSlotRepository.save(parkingSlot);
//    }

    public ParkingSlot createParkingSlot(Long floorId, String slotNumber, Long vehicleTypeId) {
        ParkingFloor parkingFloor = parkingFloorRepository.findById(floorId)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with ID: " + floorId));

        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle Type not found with ID: " + vehicleTypeId));

        // --- NEW VALIDATION LOGIC ---
        if (parkingSlotRepository.existsBySlotNumberAndParkingFloor(slotNumber, parkingFloor)) {
            throw new IllegalArgumentException("Slot number '" + slotNumber + "' already exists on floor '" + parkingFloor.getFloorNumber() + "'.");
        }
        // --- END OF NEW LOGIC ---

        String expectedPrefix = parkingFloor.getFloorNumber() + "-";
        if (!slotNumber.startsWith(expectedPrefix)) {
            throw new IllegalArgumentException(
                    "Slot name '" + slotNumber + "' does not match the selected floor '" + parkingFloor.getFloorNumber() + "'. Please use a name like '" + expectedPrefix + "01'."
            );
        }

        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setSlotNumber(slotNumber);
        parkingSlot.setParkingFloor(parkingFloor);
        parkingSlot.setVehicleType(vehicleType);
        parkingSlot.setSlotStatus(SlotStatus.AVAILABLE);

        return parkingSlotRepository.save(parkingSlot);
    }

    public List<ParkingFloor> getDashboardOverview() {
        return parkingFloorRepository.findAll();
    }

    public void deleteSlot(Long slotId) {

        if (!parkingSlotRepository.existsById(slotId)) {
            throw new ResourceNotFoundException("Parking slot not found with ID: " + slotId);
        }
        parkingSlotRepository.deleteById(slotId);
    }

    public ParkingSlot updateSlotStatus(Long slotId, SlotStatus status) {
        ParkingSlot slot = parkingSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found with ID: " + slotId));

        slot.setSlotStatus(status);
        return parkingSlotRepository.save(slot);
    }
}