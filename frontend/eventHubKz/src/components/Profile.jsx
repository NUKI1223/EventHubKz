import React, { useState, useEffect } from 'react';
import { Link, useParams, useNavigate, useLocation } from 'react-router-dom';
import api from '../api';
import { jwtDecode } from 'jwt-decode';
import '../css/Profile.css';
import github_icon from "../assets/images/github_icon.png";
import telegram_icon from "../assets/images/telegram_icon.png";
import facebook_icon from "../assets/images/facebook_icon.png";
import instagram_icon from "../assets/images/instagram_icon.png";
import LikedEvents from './LikedEvents';

const UserProfile = () => {
  const { username: routeUsername } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [avatarFile, setAvatarFile] = useState(null);
  const [avatarLoading, setAvatarLoading] = useState(false);
  const [avatarError, setAvatarError] = useState(null);

  let currentUser = null;
  const token = localStorage.getItem('token');
  if (token) {
    try {
      currentUser = jwtDecode(token);
    } catch (err) {
      console.error('Ошибка декодирования токена:', err);
    }
  }

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  useEffect(() => {
    const fetchUser = async () => {
      try {
        setLoading(true);
        let res;
        if (routeUsername) {
          res = await api.get(`/api/users/${routeUsername}`);
        } else {
          res = await api.get('/api/users/me');
        }
        console.log('Полученные данные пользователя:', JSON.stringify(res.data, null, 2));
        setUser(res.data);
      } catch (err) {
        console.error(err);
        setError('Ошибка при загрузке данных пользователя');
      } finally {
        setLoading(false);
      }
    };
    fetchUser();
  }, [routeUsername, location.pathname]);

  const isOwnProfile =
    currentUser &&
    currentUser.sub &&
    user &&
    user.username &&
    currentUser.sub.toLowerCase() === user.username.toLowerCase();

  const getUsernameFromLink = (link, prefix) => {
    if (!link) return '';
    return link.startsWith(prefix) ? link.replace(prefix, '') : link;
  };

  const handleAvatarChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setAvatarFile(file);
    }
  };

  const handleAvatarUpload = async () => {
    if (!avatarFile) return;

    setAvatarLoading(true);
    setAvatarError(null);

    try {
      // Формируем объект user, как в EditProfile.jsx
      const updatedUser = {
        username: user.username || '',
        email: user.email || '',
        description: user.description || '',
        contacts: user.contacts || {
          additionalProp1: '',
          additionalProp2: '',
          additionalProp3: '',
          additionalProp4: '',
        },
      };

      const jsonBlob = new Blob([JSON.stringify(updatedUser)], { type: 'application/json' });
      const data = new FormData();
      data.append('user', jsonBlob);
      data.append('file', avatarFile);

      console.log('Отправляемые данные для аватара:', {
        user: updatedUser,
        file: avatarFile.name,
      });

      const response = await api.put('/api/users', data, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      console.log('Ответ от API (обновление аватара):', JSON.stringify(response.data, null, 2));

      // Обновляем данные пользователя
      const updatedUserData = await api.get('/api/users/me');
      console.log('Обновленные данные пользователя:', JSON.stringify(updatedUserData.data, null, 2));
      setUser({
        ...updatedUserData.data,
        avatarUrl: updatedUserData.data.avatarUrl
          ? `${updatedUserData.data.avatarUrl}?t=${new Date().getTime()}`
          : updatedUserData.data.avatarUrl, // Добавляем параметр для избежания кэширования
      });
      setAvatarFile(null);
    } catch (err) {
      console.error('Ошибка обновления аватара:', err);
      if (err.response) {
        console.error('Детали ошибки:', JSON.stringify(err.response.data, null, 2));
        setAvatarError(`Ошибка при обновлении аватара: ${err.response.data.message || err.response.statusText}`);
      } else {
        setAvatarError('Ошибка при обновлении аватара: нет ответа от сервера');
      }
    } finally {
      setAvatarLoading(false);
    }
  };

  useEffect(() => {
    if (avatarFile) {
      handleAvatarUpload();
    }
  }, [avatarFile]);

  if (loading) return <p>Загрузка профиля...</p>;
  if (error) return <p>{error}</p>;
  if (!user) return <p>Пользователь не найден</p>;

  return (
    <div className="user-profile">
      <div className="profile-container">
        <div className="profile-sidebar">
          <div className="avatar-container">
            {user.avatarUrl ? (
              <img src={user.avatarUrl} alt={user.username} className="profile-avatar" />
            ) : (
              <div className="profile-avatar-placeholder"></div>
            )}
            {isOwnProfile && (
              <>
                <div className="avatar-overlay">
                  <span className="avatar-overlay-text">Изменить аватарку</span>
                </div>
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleAvatarChange}
                  className="avatar-input"
                  disabled={avatarLoading}
                />
              </>
            )}
          </div>
          {avatarError && <p className="error-message">{avatarError}</p>}
          <h2>{user.username || 'Имя не указано'}</h2>
          <div className="profile-actions">
          </div>
          <p className="location">{user.address || 'Социальные сети'}</p>
          <div className="social-links">
            {user.contacts?.additionalProp2 && (
              <p>
                <img src={github_icon} alt="GitHub" className="social-icon" />
                <a href={user.contacts.additionalProp2} target="_blank" rel="noopener noreferrer">
                  {getUsernameFromLink(user.contacts.additionalProp2, 'https://github.com/')}
                </a>
              </p>
            )}
            {user.contacts?.additionalProp1 && (
              <p>
                <img src={telegram_icon} alt="Telegram" className="social-icon" />
                <a href={user.contacts.additionalProp1} target="_blank" rel="noopener noreferrer">
                  {getUsernameFromLink(user.contacts.additionalProp1, 'https://t.me/')}
                </a>
              </p>
            )}
            {user.contacts?.additionalProp3 && (
              <p>
                <img src={instagram_icon} alt="Instagram" className="social-icon" />
                <a href={user.contacts.additionalProp3} target="_blank" rel="noopener noreferrer">
                  {getUsernameFromLink(user.contacts.additionalProp3, 'https://instagram.com/')}
                </a>
              </p>
            )}
            {user.contacts?.additionalProp4 && (
              <p>
                <img src={facebook_icon} alt="Facebook" className="social-icon" />
                <a href={user.contacts.additionalProp4} target="_blank" rel="noopener noreferrer">
                  {getUsernameFromLink(user.contacts.additionalProp4, 'https://facebook.com/')}
                </a>
              </p>
            )}
          </div>
        </div>

        <div className="profile-details">
          <div className="profile-details-content">
            <h3>Имя</h3>
            <p>{user.username || 'Не указано'}</p>
            <hr className="divider" />
            <h3>Почта</h3>
            <p>{user.email || 'Не указано'}</p>
            <hr className="divider" />
            <h3>Описание</h3>
            <p>{user.description || 'Не указано'}</p>
            {isOwnProfile && (
              <button className="edit-btn" onClick={() => navigate('/edit-profile')}>
                Редактировать
              </button>
            )}
          </div>
        </div>
      </div>

      <LikedEvents />
    </div>
  );
};

export default UserProfile;