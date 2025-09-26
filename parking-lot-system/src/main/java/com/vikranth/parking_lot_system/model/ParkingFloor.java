package com.vikranth.parking_lot_system.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "parking_floors")
public class ParkingFloor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String floorNumber; // e.g., "G", "B1", "F1"

    @JsonManagedReference
    @OneToMany(mappedBy = "parkingFloor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ParkingSlot> parkingSlots = new HashSet<>();

    public ParkingFloor() {
    }

    public ParkingFloor(String floorNumber) {
        this.floorNumber = floorNumber;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Set<ParkingSlot> getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(Set<ParkingSlot> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;
        ParkingFloor that = (ParkingFloor) otherObject;
        return Objects.equals(floorNumber, that.floorNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(floorNumber);
    }

    @Override
    public String toString() {
        return "ParkingFloor{" +
                "id=" + id +
                ", floorNumber='" + floorNumber + '\'' +
                '}';
    }
}