import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';    // именованный импорт
import SearchBar from './SearchBar';
import NotificationsDropdown from './NotificationsDropdown';
import api from '../api';
import '../css/Navbar.css';

const Navbar = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);

  let username = null;
  let currentUser = null;
  if (token) {
    try {
      currentUser = jwtDecode(token);
      username = currentUser.sub;
    } catch (err) {
      console.error('Ошибка декодирования токена:', err);
    }
  }


  useEffect(() => {
    if (!username) return;
    api.get('/api/notifications', { params: { username } })
      .then(res => {
        setNotifications(res.data);
        setUnreadCount(res.data.filter(n => !n.read).length);
      })
      .catch(err => console.error('Ошибка загрузки уведомлений:', err));
  }, [username]);

  const toggleNotifications = () => {
    setShowNotifications(v => !v);
    if (unreadCount > 0) setUnreadCount(0);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/">EventHubKz</Link>
      </div>
      <div className="navbar-menu">
        <ul>
          <li><Link to="/">Главная</Link></li>
          {token && (
            <>
              <li><Link to={`/profile/${username}`}>Профиль</Link></li>
              <li><Link to="/request-event">Создать заявку</Link></li>
            </>
          )}
          {currentUser?.role === 'ADMIN' && (
            <li><Link to="/admin">Админ панель</Link></li>
          )}
        </ul>
      </div>
      <div className="navbar-search">
        <SearchBar />
      </div>
      <div className="navbar-notifications">
        {token && (
          <button className="notification-btn" onClick={toggleNotifications}>
            🔔
            {unreadCount > 0 && (
              <span className="notification-badge">{unreadCount}</span>
            )}
          </button>
        )}
        {showNotifications && token && (
          <NotificationsDropdown
            notifications={notifications}
            username={username}
            onClose={() => setShowNotifications(false)}
            onUpdate={() => {
              api.get('/api/notifications', { params: { username } })
                .then(res => {
                  setNotifications(res.data);
                  setUnreadCount(res.data.filter(n => !n.read).length);
                });
            }}
          />
        )}
      </div>
      <div className="navbar-auth">
        {token ? (
          <button onClick={handleLogout}>Выход</button>
        ) : (
          <>
            <Link to="/login">Вход</Link>
            <Link to="/signup">Регистрация</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
