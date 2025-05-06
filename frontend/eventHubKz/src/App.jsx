import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import './css/Form.css';
import EventList from './components/EventList';
import EventDetail from './components/EventDetail';
import EventRequestForm from './components/EventRequestForm';
import SearchResults from './components/SearchResults'
import UserProfile from './components/Profile';
import LikedEvents from './components/LikedEvents';
import EditProfile from './components/EditProfile'; 
import AdminRoute from './components/AdminRoute';
import AdminDashboard from './components/AdminDashboard';
import VerifyCode from './components/VerifyCode';
import SignUpNew from './components/SignUpNew';
import SignIn from './components/SignIn';
import Verification from './components/Verification';
import Header from './components/Header';
import MainPage from './components/MainPage';
import Support from './components/Support';

function AppContent() {
  const location = useLocation();

  const hideNavbarPaths = ['/signupnew', '/signin', '/verification', '/header', '/login', '/signup'];

  const shouldShowNavbar = !hideNavbarPaths.includes(location.pathname);

  return (
    <>
      {shouldShowNavbar && <Header />}
      <Routes>
        <Route path="/eventlist" element={<EventList />} />
        <Route path="/login" element={<SignIn />} />
        <Route path="/signup" element={<SignUpNew />} />
        <Route path="/" element={<MainPage />} />
        <Route path="/request-event" element={<EventRequestForm />} />
        <Route path="/profile/:username?" element={<UserProfile />} />
        <Route path="/profile/:username/liked-events" element={<LikedEvents />} />
        <Route path="/verify" element={<VerifyCode />} />
        <Route path="/edit-profile" element={<EditProfile />} />
        <Route path="/events/:id" element={<EventDetail />} />
        <Route path="/search" element={<SearchResults />} />
        <Route path="/admin" element={<AdminRoute><AdminDashboard /></AdminRoute>} />
        <Route path="/signupnew" element={<SignUpNew />} /> 
        <Route path="/signin" element={<SignIn />} /> 
        <Route path="/verification" element={<Verification />} /> 
        <Route path="/main" element={<MainPage />} />
        <Route path="/support" element={<Support />} />
      </Routes>
    </>
  );
}

function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

export default App;
