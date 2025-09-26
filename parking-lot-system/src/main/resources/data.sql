-- This idempotent script inserts initial data only if it doesn't already exist.

-- Insert vehicle types
INSERT INTO vehicle_types (name, hourly_rate)
SELECT '4 wheeler', 30.00 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM vehicle_types WHERE name = '4 wheeler');

INSERT INTO vehicle_types (name, hourly_rate)
SELECT '2 wheeler', 20.00 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM vehicle_types WHERE name = '2 wheeler');

-- Insert parking floors
INSERT INTO parking_floors (floor_number)
SELECT 'G' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_floors WHERE floor_number = 'G');

INSERT INTO parking_floors (floor_number)
SELECT 'B1' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_floors WHERE floor_number = 'B1');

-- Insert parking slots for the Ground floor (ID 1)
INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'G-01', 'AVAILABLE', 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'G-01');

INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'G-02', 'AVAILABLE', 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'G-02');

INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'G-03', 'AVAILABLE', 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'G-03');

INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'G-04', 'AVAILABLE', 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'G-04');

INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'G-05', 'AVAILABLE', 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'G-05');

INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'G-06', 'AVAILABLE', 1, 2 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'G-06');

INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'G-07', 'AVAILABLE', 1, 2 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'G-07');

INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'G-08', 'AVAILABLE', 1, 2 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'G-08');


-- Insert parking slots for the Basement 1 floor (ID 2)
INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'B1-01', 'AVAILABLE', 2, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'B1-01');

INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'B1-02', 'AVAILABLE', 2, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'B1-02');

INSERT INTO parking_slots (slot_number, slot_status, parking_floor_id, vehicle_type_id)
SELECT 'B1-03', 'AVAILABLE', 2, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM parking_slots WHERE slot_number = 'B1-03');