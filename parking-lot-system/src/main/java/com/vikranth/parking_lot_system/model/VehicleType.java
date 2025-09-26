package com.vikranth.parking_lot_system.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "vehicle_types")
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // e.g., "4 wheeler", "2 wheeler"

    @Column(nullable = false)
    private BigDecimal hourlyRate; // For Financial Calculations

    public VehicleType() {
    }

    public VehicleType(String name, BigDecimal hourlyRate) {
        this.name = name;
        this.hourlyRate = hourlyRate;
    }

    public Long getId() { // Gets Id
        return id;
    }

    public void setId(Long id) { // sets Id
        this.id = id;
    }

    public String getName() { // gets name
        return name;
    }

    public void setName(String name) { //sets name
        this.name = name;
    }

    public BigDecimal getHourlyRate() { // gets hourly rate
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) { // sets hourly rate
        this.hourlyRate = hourlyRate;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;

        if (otherObject == null || this.getClass() != otherObject.getClass()) return false;

        VehicleType otherVehicleType = (VehicleType) otherObject;

        return Objects.equals(this.name, otherVehicleType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "VehicleType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hourlyRate=" + hourlyRate +
                '}';
    }
}