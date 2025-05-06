import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import api from "../api";
import SearchBar from "./SearchBar";
import NotificationsDropdown from "./NotificationsDropdown"; // Импортируем компонент
import "../css/Navbar.css";

function Header() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [isShaking, setIsShaking] = useState(false);
  const [user, setUser] = useState(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const [showLogoutConfirm, setShowLogoutConfirm] = useState(false);
  const [dropdownPosition, setDropdownPosition] = useState("right"); // Новое состояние для позиции

  const notificationRef = useRef(null);
  const dropdownRef = useRef(null);

  let username = null;
  let currentUser = null;
  if (token) {
    try {
      currentUser = jwtDecode(token);
      username = currentUser.sub;
    } catch (err) {
      console.error("Ошибка декодирования токена:", err);
    }
  }

  useEffect(() => {
    if (!username) return;
    api
      .get("/api/users/me")
      .then((res) => {
        setUser(res.data);
      })
      .catch((err) => console.error("Ошибка загрузки данных пользователя:", err));
  }, [username]);

  useEffect(() => {
    if (!username) return;
    api
      .get("/api/notifications", { params: { username } })
      .then((res) => {
        setNotifications(res.data);
        setUnreadCount(res.data.filter((n) => !n.read).length);
      })
      .catch((err) => console.error("Ошибка загрузки уведомлений:", err));
  }, [username]);

  useEffect(() => {
    if (!showDropdown || !dropdownRef.current) return;

    const dropdown = dropdownRef.current.querySelector(".user-dropdown");
    const rect = dropdown.getBoundingClientRect();
    const windowWidth = window.innerWidth;

    // Если правый край меню выходит за пределы окна, выравниваем по левому краю
    if (rect.right > windowWidth) {
      setDropdownPosition("left");
    } else {
      setDropdownPosition("right");
    }
  }, [showDropdown]);

  const toggleNotifications = () => {
    setShowNotifications((v) => !v);
    setIsShaking(true);
    setTimeout(() => setIsShaking(false), 500);
    if (unreadCount > 0) setUnreadCount(0);
  };

  const toggleDropdown = () => {
    setShowDropdown((prev) => !prev);
  };

  const handleLogout = () => {
    setShowLogoutConfirm(true);
  };

  const confirmLogout = () => {
    localStorage.removeItem("token");
    setUser(null);
    setShowDropdown(false);
    setShowLogoutConfirm(false);
    navigate("/signin");
  };

  const cancelLogout = () => {
    setShowLogoutConfirm(false);
  };

  const handleUpdateNotifications = () => {
    if (!username) return;
    api
      .get("/api/notifications", { params: { username } })
      .then((res) => {
        setNotifications(res.data);
        setUnreadCount(res.data.filter((n) => !n.read).length);
      })
      .catch((err) => console.error("Ошибка обновления уведомлений:", err));
  };

  useEffect(() => {
    function handleClickOutside(event) {
      if (notificationRef.current && !notificationRef.current.contains(event.target)) {
        setShowNotifications(false);
      }
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    }

    if (showNotifications || showDropdown) {
      document.addEventListener("mousedown", handleClickOutside);
    } else {
      document.removeEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [showNotifications, showDropdown]);

  return (
    <section className="header">
      <div className="header__title">
        <Link to="/">EventHubKZ</Link>
      </div>
      <div className="header__navbar">
        <ul className="navbar-menu">
          <li>
            <Link to="/">Главная</Link>
          </li>
          {token && (
            <>
              <li>
                <Link to="/eventlist">Мероприятия</Link>
              </li>
              <li>
                <Link to="/request-event">Создать заявку</Link>
              </li>
              <li>
                <Link to="/support">Поддержка</Link>
              </li>
              {currentUser?.role === "ADMIN" && (
                <li>
                  <Link to="/admin">Админ панель</Link>
                </li>
              )}
            </>
          )}
        </ul>
        <SearchBar />
        <div className="header__notifications" ref={notificationRef}>
          {token && (
            <div className="notification-container">
              <button
                className={`notification-btn ${isShaking ? "shake" : ""}`}
                onClick={toggleNotifications}
              >
                🔔
                {unreadCount > 0 && (
                  <span className="notification-badge">{unreadCount}</span>
                )}
              </button>
              {showNotifications && (
                <NotificationsDropdown
                  notifications={notifications}
                  username={username}
                  onClose={() => setShowNotifications(false)}
                  onUpdate={handleUpdateNotifications}
                />
              )}
            </div>
          )}
        </div>
        <div className="user-section" ref={dropdownRef}>
          {token ? (
            <div className="user-profile-link" onClick={toggleDropdown}>
              <div className="user-avatar-link">
                {user?.avatarUrl ? (
                  <img src={user.avatarUrl} alt={username} className="user-avatar" />
                ) : (
                  <div className="user-avatar-placeholder"></div>
                )}
                <span className="user-name">{username}</span>
              </div>
              {showDropdown && (
                <ul className={`user-dropdown ${dropdownPosition}`}>
                  <li>
                    <Link to={`/profile/${username}`} className="dropdown-item">
                      Профиль
                    </Link>
                  </li>
                  <li>
                    <button onClick={handleLogout} className="dropdown-item">
                      Выход
                    </button>
                  </li>
                </ul>
              )}
            </div>
          ) : (
            <div className="header__authorization">
              <Link to="/signin">Вход</Link>
              <Link to="/signupnew">Регистрация</Link>
            </div>
          )}
        </div>
      </div>

      {showLogoutConfirm && (
        <div className="logout-confirm-overlay">
          <div className="logout-confirm-dialog">
            <h3 className="logout-confirm-title">Подтверждение выхода</h3>
            <p className="logout-confirm-message">Вы уверены, что хотите выйти?</p>
            <div className="logout-confirm-buttons">
              <button onClick={confirmLogout} className="logout-confirm-btn confirm">
                Да, выйти
              </button>
              <button onClick={cancelLogout} className="logout-confirm-btn cancel">
                Отмена
              </button>
            </div>
          </div>
        </div>
      )}
    </section>
  );
}

export default Header;