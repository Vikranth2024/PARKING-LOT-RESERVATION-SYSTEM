// src/components/UserDashboard.jsx
import { useState, useEffect } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import { motion, AnimatePresence } from 'framer-motion';
import * as api from '../api/api';

const SearchIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clipRule="evenodd" /></svg>;
const CheckCircleIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" /></svg>;


export default function UserDashboard() {
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [vehicleType, setVehicleType] = useState('4 wheeler');
  const [slots, setSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [vehicleNumber, setVehicleNumber] = useState('');
  const [loading, setLoading] = useState(false);
  const [calculatedPrice, setCalculatedPrice] = useState(null);
  
  const [reservations, setReservations] = useState([]);

  const listVariants = { hidden: { opacity: 0 }, visible: { opacity: 1, transition: { staggerChildren: 0.05 } } };
  const itemVariants = { hidden: { y: 20, opacity: 0 }, visible: { y: 0, opacity: 1 } };

  const fetchActiveReservations = async () => {
    try {
      const activeReservations = await api.getActiveReservations();
      setReservations(activeReservations);
    } catch (error) {
      toast.error("Could not fetch active reservations.");
    }
  };

  useEffect(() => {
    fetchActiveReservations();
  }, []);

  const handleFetchAvailability = async () => { if (!startTime || !endTime) return toast.error("Please select both a start and end time."); setLoading(true); setSelectedSlot(null); setCalculatedPrice(null); try { const data = await api.fetchAvailableSlots(startTime, endTime); const fetchedSlots = Array.isArray(data) ? data : data.content; if (fetchedSlots.length === 0) toast.info("No slots available for this time range."); setSlots(fetchedSlots); } catch (error) { toast.error(error.message); } setLoading(false); };
  const handleSlotSelect = (slot) => { setSelectedSlot(slot); const start = new Date(startTime); const end = new Date(endTime); const durationMinutes = (end - start) / (1000 * 60); if (durationMinutes <= 0) { toast.error("End time must be after start time."); setCalculatedPrice(0); return; } const hours = Math.ceil(durationMinutes / 60); const rate = slot.vehicleTypeName === '4 wheeler' ? 30 : 20; setCalculatedPrice(hours * rate); };
  
  const handleCreateReservation = async () => {
    if (!vehicleNumber) return toast.warn('Please enter a vehicle number.');
    setLoading(true);
    try {
      const reservationData = { slotId: selectedSlot.slotId, vehicleNumber, startTime, endTime };
      const newReservation = await api.createReservation(reservationData);
      toast.success(`Success! Reservation confirmed with ID: ${newReservation.reservationId}`);
      setSelectedSlot(null);
      setVehicleNumber('');
      setCalculatedPrice(null);
      await handleFetchAvailability(); 
      await fetchActiveReservations(); 
    } catch (error) {
      toast.error(error.message);
    }
    setLoading(false);
  };
  
  const handleCancelReservation = async (reservationId) => {
    if (!window.confirm("Are you sure you want to cancel this reservation?")) return;
    try {
      await api.cancelReservation(reservationId);
      toast.success("Reservation cancelled successfully.");
      await handleFetchAvailability(); 
      await fetchActiveReservations(); 
    } catch (error) {
      toast.error(error.message);
    }
  };

  const filteredSlots = slots.filter(slot => slot.vehicleTypeName === vehicleType);

  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.5 }} className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-3xl">
      
      <AnimatePresence>
        {reservations.length > 0 && (
          <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} className="mb-8 p-4 bg-gray-50 rounded-lg border">
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Your Active Bookings</h2>
            <div className="space-y-3">
              {reservations.map(res => (
                <motion.div key={res.reservationId} layout variants={itemVariants} className="p-3 bg-white rounded-md shadow-sm border flex justify-between items-center">
                  <div>
                    <p className="font-bold">Slot: {res.slotNumber} (Floor: {res.floorNumber})</p>
                    <p className="text-sm text-gray-600">Vehicle: {res.vehicleNumber}</p>
                    <p className="text-xs text-gray-500">From: {new Date(res.startTime).toLocaleString()} To: {new Date(res.endTime).toLocaleString()}</p>
                  </div>
                  <motion.button whileHover={{ scale: 1.1 }} whileTap={{ scale: 0.9 }} onClick={() => handleCancelReservation(res.reservationId)} className="bg-red-500 text-white text-xs font-bold py-1 px-3 rounded-full hover:bg-red-600">
                    Cancel
                  </motion.button>
                </motion.div>
              ))}
            </div>
          </motion.div>
        )}
      </AnimatePresence>
      
      <h1 className="text-4xl font-light text-center text-gray-800 mb-2">Book a Parking Slot</h1>
      <p className="text-center text-gray-500 mb-8">Find and reserve your spot in just a few clicks.</p>
      

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4 items-end p-6 bg-gray-50/70 rounded-lg border">
        <div>
          <label htmlFor="startTime" className="block text-sm font-medium text-gray-700 mb-1">Start Time</label>
          <input id="startTime" type="datetime-local" value={startTime} onChange={(e) => setStartTime(e.target.value)} className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 transition-shadow" />
        </div>
        <div>
          <label htmlFor="endTime" className="block text-sm font-medium text-gray-700 mb-1">End Time</label>
          <input id="endTime" type="datetime-local" value={endTime} onChange={(e) => setEndTime(e.target.value)} className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 transition-shadow" />
        </div>
        <div>
          <label htmlFor="vehicleType" className="block text-sm font-medium text-gray-700 mb-1">Vehicle Type</label>
          <select id="vehicleType" value={vehicleType} onChange={(e) => setVehicleType(e.target.value)} className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 transition-shadow">
            <option value="4 wheeler">4 Wheeler</option>
            <option value="2 wheeler">2 Wheeler</option>
          </select>
        </div>
      </div>
      <motion.button whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }} onClick={handleFetchAvailability} disabled={loading} className="w-full flex items-center justify-center bg-indigo-600 text-white font-bold py-3 px-4 rounded-lg hover:bg-indigo-700 disabled:bg-gray-400 transition-colors shadow-lg">
        <SearchIcon /> {loading ? 'Searching...' : 'Check Availability'}
      </motion.button>

    
      <AnimatePresence>
        {filteredSlots.length > 0 && (
          <motion.div variants={listVariants} initial="hidden" animate="visible" className="mt-8">
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Please select a slot:</h2>
            <div className="grid grid-cols-3 md:grid-cols-5 gap-3">
              {filteredSlots.map((slot) => (
                <motion.button key={slot.slotId} variants={itemVariants} whileHover={{ scale: 1.1, y: -5 }} whileTap={{ scale: 0.95 }} onClick={() => handleSlotSelect(slot)} className={`p-3 rounded-lg text-center font-bold border-2 shadow-sm ${selectedSlot?.slotId === slot.slotId ? 'bg-indigo-600 text-white border-indigo-600' : 'bg-gray-50 hover:border-gray-300'}`}>
                  {slot.slotNumber}
                  <div className="text-xs font-normal text-gray-500">{slot.floorNumber}</div>
                </motion.button>
              ))}
            </div>
          </motion.div>
        )}
      </AnimatePresence>

      <AnimatePresence>
        {selectedSlot && (
          <motion.div initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: 'auto' }} exit={{ opacity: 0, height: 0 }} className="mt-6 pt-6 border-t-2 overflow-hidden">
            <h3 className="text-xl font-semibold">Confirm Booking for Slot: <span className="text-indigo-600">{selectedSlot.slotNumber}</span></h3>
            <div className="my-3 p-3 bg-blue-50 border border-blue-200 rounded-lg text-center">
              Estimated Cost: <span className="font-bold text-lg">Rs. {calculatedPrice}</span>
            </div>
            <label htmlFor="vehicleNumber" className="block text-sm font-medium text-gray-700 mb-1">Vehicle Number</label>
            <input type="text" id="vehicleNumber" value={vehicleNumber} onChange={(e) => setVehicleNumber(e.target.value)} placeholder="e.g., KA01AB1234" className="w-full p-2 border border-gray-300 rounded-lg mb-4 focus:ring-2 focus:ring-green-500" />
            <motion.button whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }} onClick={handleCreateReservation} disabled={loading} className="w-full flex items-center justify-center bg-green-600 text-white font-bold py-3 px-4 rounded-lg hover:bg-green-700 disabled:bg-gray-400 shadow-lg">
              <CheckCircleIcon/> {loading ? 'Reserving...' : 'Book Now'}
            </motion.button>
          </motion.div>
        )}
      </AnimatePresence>
    </motion.div>
  );
}