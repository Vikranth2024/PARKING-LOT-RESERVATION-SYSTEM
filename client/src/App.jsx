import { useState, useEffect } from 'react';
import { ToastContainer } from 'react-toastify';
import UserDashboard from './components/UserDashboard';
import AdminDashboard from './components/AdminDashboard';

function App() {
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    // Check the URL path to set the role
    if (window.location.pathname.startsWith('/admin')) {
      setIsAdmin(true);
    } else {
      setIsAdmin(false);
    }
  }, []);

  return (
    <>
      <ToastContainer position="top-center" autoClose={3000} hideProgressBar={false} />
      <div className="bg-slate-100 min-h-screen flex items-center justify-center font-sans p-4">
        {isAdmin ? <AdminDashboard /> : <UserDashboard />}
      </div>
    </>
  );
}

export default App;