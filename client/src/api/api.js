const API_BASE_URL = 'http://localhost:8080/api';

const request = async (endpoint, options = {}) => {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
  if (!response.ok) {
    if (response.status === 204) return null; 
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || `An error occurred: ${response.statusText}`);
  }
  return response.status === 204 ? null : response.json();
};

export const fetchAvailableSlots = (startTime, endTime) => {
  const params = new URLSearchParams({ startTime, endTime });
  return request(`/availability?${params}`);
};

export const createReservation = (reservationData) => {
  return request('/reserve', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(reservationData),
  });
};

export const getAdminOverview = () => {
  return request('/admin/overview');
};

export const addFloor = (floorData) => {
  return request('/admin/floors', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(floorData),
  });
};

export const addSlot = (slotData) => {
  return request('/admin/slots', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(slotData),
  });
};

export const updateSlotStatus = (slotId, status) => {
  const params = new URLSearchParams({ status });
  return request(`/admin/slots/${slotId}/status?${params}`, {
    method: 'PUT',
  });
};

export const getActiveReservations = () => {
  return request('/reservations/active');
};

export const cancelReservation = (reservationId) => {
  return request(`/reservations/${reservationId}`, {
    method: 'DELETE',
  });
};

export const deleteSlot = (slotId) => {
  return request(`/admin/slots/${slotId}`, {
    method: 'DELETE',
  });
};