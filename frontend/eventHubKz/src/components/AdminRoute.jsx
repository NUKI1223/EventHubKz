
import React from 'react';
import { Navigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';



const AdminRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  const decoded = jwtDecode(token);
  if (!token) return <Navigate to="/login" />;
  try {
    const user = jwtDecode(token);
    if (user.role !== 'ADMIN') return <Navigate to="/not-authorized" />;
    return children;
  } catch (error) {
    return <Navigate to="/login" />;
  }
};

export default AdminRoute;
