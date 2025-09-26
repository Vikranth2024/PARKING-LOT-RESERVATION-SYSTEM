// src/components/AdminDashboard.jsx
import { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { motion, AnimatePresence } from 'framer-motion';
import * as api from '../api/api';

// Simple SVG Icon Components
const PlusIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clipRule="evenodd" /></svg>;
const TrashIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>;
const WrenchIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.096 2.572-1.065z" /><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /></svg>;
const CheckCircleIcon = () => <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>;


export default function AdminDashboard() {
  const [floorNumber, setFloorNumber] = useState('');
  const [newSlotNumber, setNewSlotNumber] = useState('');
  const [selectedFloorId, setSelectedFloorId] = useState('');
  const [selectedVehicleTypeId, setSelectedVehicleTypeId] = useState('1');
  const [floors, setFloors] = useState([]);
  const [loading, setLoading] = useState(false);

  // Animation variants for Framer Motion
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { staggerChildren: 0.1 }
    }
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: { y: 0, opacity: 1 }
  };

  const fetchOverview = async () => { /* ... same as before ... */ setLoading(true); try { const overviewData = await api.getAdminOverview(); setFloors(overviewData); } catch (error) { toast.error("Failed to fetch parking lot overview."); } finally { setLoading(false); }};
  useEffect(() => { fetchOverview(); }, []);
  const handleAddFloor = async (e) => { e.preventDefault(); /* ... same as before ... */ setLoading(true); try { const newFloor = await api.addFloor({ floorNumber }); toast.success(`Floor "${newFloor.floorNumber}" added!`); setFloors(prev => [...prev, newFloor]); setFloorNumber(''); } catch (err) { toast.error(err.message); } setLoading(false); };
  const handleAddSlot = async (e) => { e.preventDefault(); /* ... same as before ... */ if (!selectedFloorId) return toast.warn("Please select a floor."); setLoading(true); try { await api.addSlot({ floorId: selectedFloorId, slotNumber: newSlotNumber, vehicleTypeId: selectedVehicleTypeId }); toast.success(`Slot "${newSlotNumber}" created successfully!`); fetchOverview(); setNewSlotNumber(''); } catch (err) { toast.error(err.message); } setLoading(false); };
  const handleUpdateStatus = async (slotId, status) => { /* ... same as before ... */ if (!window.confirm(`Are you sure you want to set this slot to ${status}?`)) return; try { await api.updateSlotStatus(slotId, status); toast.success(`Slot status updated!`); fetchOverview(); } catch (error) { toast.error(error.message); }};
  const handleDeleteSlot = async (slotId) => { /* ... same as before ... */ if (!window.confirm("Are you sure?")) return; try { await api.deleteSlot(slotId); toast.success(`Slot deleted successfully.`); fetchOverview(); } catch (error) { toast.error(error.message); }};

  const getStatusClasses = (status) => {
    switch (status) {
      case 'AVAILABLE': return 'bg-green-100 text-green-800 border-green-300';
      case 'RESERVED': return 'bg-yellow-100 text-yellow-800 border-yellow-300';
      case 'MAINTENANCE': return 'bg-red-100 text-red-800 border-red-300';
      default: return 'bg-gray-100';
    }
  };

  return (
    <motion.div 
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5 }}
      className="bg-white p-8 rounded-xl shadow-2xl w-full max-w-6xl"
    >
      <h1 className="text-4xl font-light text-center text-gray-800 mb-8">Admin Dashboard</h1>

      {/* Forms Section */}
      <motion.div layout className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10 p-6 bg-gray-50/50 border rounded-xl">
        <form onSubmit={handleAddFloor} className="space-y-3">
          <h2 className="text-xl font-semibold text-gray-700">Add Floor</h2>
          <input type="text" value={floorNumber} onChange={e => setFloorNumber(e.target.value)} placeholder="Floor Name (e.g., F3)" className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" required />
          <motion.button whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }} type="submit" disabled={loading} className="w-full flex items-center justify-center bg-blue-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-blue-700 disabled:bg-gray-400 transition-colors">
            <PlusIcon /> Add Floor
          </motion.button>
        </form>
        <form onSubmit={handleAddSlot} className="space-y-3 md:col-span-2">
           <h2 className="text-xl font-semibold text-gray-700">Add Slot</h2>
           <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
              <input type="text" value={newSlotNumber} onChange={e => setNewSlotNumber(e.target.value)} placeholder="Slot Number (e.g., F3-01)" className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" required/>
              <select value={selectedFloorId} onChange={e => setSelectedFloorId(e.target.value)} className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" required>
                  <option value="">Select Floor</option>
                  {floors.map(f => <option key={f.id} value={f.id}>{f.floorNumber}</option>)}
              </select>
              <select value={selectedVehicleTypeId} onChange={e => setSelectedVehicleTypeId(e.target.value)} className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500" required>
                 <option value="1">4 Wheeler</option>
                 <option value="2">2 Wheeler</option>
              </select>
           </div>
           <motion.button whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }} type="submit" disabled={loading} className="w-full flex items-center justify-center bg-blue-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-blue-700 disabled:bg-gray-400">
             <PlusIcon /> Add Slot
           </motion.button>
        </form>
      </motion.div>

      {/* Overview Section */}
      <h2 className="text-3xl font-light text-gray-700 mb-4">Parking Lot Overview</h2>
      <AnimatePresence>
        <motion.div 
          variants={containerVariants}
          initial="hidden"
          animate="visible"
          className="space-y-8"
        >
          {floors.map(floor => (
            <motion.div key={floor.id} variants={itemVariants}>
              <h3 className="text-2xl font-semibold border-b-2 border-gray-200 pb-2 mb-4">Floor: {floor.floorNumber}</h3>
              <div className="grid grid-cols-3 sm:grid-cols-5 md:grid-cols-8 lg:grid-cols-12 gap-3">
                {floor.parkingSlots.map(slot => (
                  <motion.div 
                    key={slot.id} 
                    layout
                    whileHover={{ scale: 1.1, zIndex: 10 }}
                    className={`p-2 rounded-lg text-center text-xs group relative border shadow-sm cursor-pointer ${getStatusClasses(slot.slotStatus)}`}
                  >
                    <div className="font-bold text-sm">{slot.slotNumber}</div>
                    <div className="capitalize">{slot.slotStatus.toLowerCase()}</div>
                    <div className="text-gray-500 italic text-[10px]">{slot.vehicleType?.name}</div>
                    <div className="absolute -top-3 -right-3 opacity-0 group-hover:opacity-100 transition-opacity flex bg-white rounded-full shadow-lg">
                        <motion.button whileTap={{ scale: 0.8 }} onClick={() => handleDeleteSlot(slot.id)} className="text-red-500 hover:text-red-700 p-1.5" title="Delete Slot"><TrashIcon /></motion.button>
                        {slot.slotStatus === 'AVAILABLE' && (
                            <motion.button whileTap={{ scale: 0.8 }} onClick={() => handleUpdateStatus(slot.id, 'MAINTENANCE')} className="text-orange-500 hover:text-orange-700 p-1.5" title="Set to Maintenance"><WrenchIcon /></motion.button>
                        )}
                       {slot.slotStatus === 'MAINTENANCE' && (
                          <motion.button whileTap={{ scale: 0.8 }} onClick={() => handleUpdateStatus(slot.id, 'AVAILABLE')} className="text-green-500 hover:text-green-700 p-1.5" title="Set to Available"><CheckCircleIcon /></motion.button>
                      )}
                    </div>
                  </motion.div>
                ))}
              </div>
            </motion.div>
          ))}
        </motion.div>
      </AnimatePresence>
    </motion.div>
  );
}