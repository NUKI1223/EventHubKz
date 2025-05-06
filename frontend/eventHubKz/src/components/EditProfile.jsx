import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import '../css/EditProfile.css';

const EditProfile = () => {
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    description: '',
    telegram: '',
    github: '',
    instagram: '',
    facebook: '',
  });
  const [originalData, setOriginalData] = useState(null);
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const res = await api.get('/api/users/me');
        const userData = {
          fullName: res.data.username || '',
          email: res.data.email || '',
          description: res.data.description || '',
          telegram: res.data.contacts?.additionalProp1?.replace('https://t.me/', '') || '',
          github: res.data.contacts?.additionalProp2?.replace('https://github.com/', '') || '',
          instagram: res.data.contacts?.additionalProp3?.replace('https://instagram.com/', '') || '',
          facebook: res.data.contacts?.additionalProp4?.replace('https://facebook.com/', '') || '',
        };
        console.log('Полученные данные пользователя:', JSON.stringify(userData, null, 2));
        setFormData(userData);
        setOriginalData(userData);
      } catch (err) {
        console.error('Ошибка загрузки данных пользователя:', err);
        setError('Ошибка загрузки данных');
      }
    };

    fetchUserData();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccessMessage('');

    try {
      const updatedUser = {
        username: formData.fullName,
        email: formData.email,
        description: formData.description,
        contacts: {
          additionalProp1: formData.telegram ? `https://t.me/${formData.telegram}` : '',
          additionalProp2: formData.github ? `https://github.com/${formData.github}` : '',
          additionalProp3: formData.instagram ? `https://instagram.com/${formData.instagram}` : '',
          additionalProp4: formData.facebook ? `https://facebook.com/${formData.facebook}` : '',
        },
      };

      const hasChanges = JSON.stringify(updatedUser) !== JSON.stringify({
        username: originalData.fullName,
        email: originalData.email,
        description: originalData.description,
        contacts: {
          additionalProp1: originalData.telegram ? `https://t.me/${originalData.telegram}` : '',
          additionalProp2: originalData.github ? `https://github.com/${originalData.github}` : '',
          additionalProp3: originalData.instagram ? `https://instagram.com/${originalData.instagram}` : '',
          additionalProp4: originalData.facebook ? `https://facebook.com/${originalData.facebook}` : '',
        },
      }) || file !== null;

      if (!hasChanges) {
        console.log('Данные не изменились, запрос не отправлен');
        setSuccessMessage('Данные не изменились');
        setLoading(false);
        setTimeout(() => {
          navigate('/profile');
        }, 1000);
        return;
      }

      console.log('Отправляемые данные пользователя:', JSON.stringify(updatedUser, null, 2));

      const jsonBlob = new Blob([JSON.stringify(updatedUser)], { type: 'application/json' });
      const data = new FormData();
      data.append('user', jsonBlob);
      if (file) {
        data.append('file', file);
      }

      const response = await api.put('/api/users', data, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      console.log('Ответ от API:', JSON.stringify(response.data, null, 2));

      setSuccessMessage('Профиль успешно обновлен!');
      setTimeout(() => {
        navigate('/profile');
      }, 1000);
    } catch (err) {
      console.error('Ошибка обновления профиля:', err);
      if (err.response) {
        console.error('Детали ошибки:', JSON.stringify(err.response.data, null, 2));
        setError(`Ошибка при обновлении профиля: ${err.response.data.message || err.response.statusText}`);
      } else {
        setError('Ошибка при обновлении профиля: нет ответа от сервера');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="edit-profile">
      <h2>Редактирование профиля</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-section">
          <h3>Личная информация</h3>
          <div className="form-group">
            <label>Полное имя:</label>
            <input
              type="text"
              name="fullName"
              value={formData.fullName}
              onChange={handleChange}
              placeholder="Введите ваше имя"
              required
            />
          </div>
          <div className="form-group">
            <label>Email:</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="Введите ваш email"
              required
            />
          </div>
          <div className="form-group">
            <label>Описание:</label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Введите описание"
            />
          </div>
        </div>

        <div className="form-section">
          <h3>Социальные сети</h3>
          <div className="form-group">
            <label>GitHub (имя пользователя):</label>
            <input
              type="text"
              name="github"
              value={formData.github}
              onChange={handleChange}
              placeholder="Например, username"
            />
          </div>
          <div className="form-group">
            <label>Telegram (имя пользователя):</label>
            <input
              type="text"
              name="telegram"
              value={formData.telegram}
              onChange={handleChange}
              placeholder="Например, username"
            />
          </div>
          <div className="form-group">
            <label>Instagram (имя пользователя):</label>
            <input
              type="text"
              name="instagram"
              value={formData.instagram}
              onChange={handleChange}
              placeholder="Например, username"
            />
          </div>
          <div className="form-group">
            <label>Facebook (имя пользователя):</label>
            <input
              type="text"
              name="facebook"
              value={formData.facebook}
              onChange={handleChange}
              placeholder="Например, username"
            />
          </div>
        </div>

        <div className="form-section">
          <h3>Аватар</h3>
          <div className="form-group">
            <label>Загрузить аватар:</label>
            <input type="file" onChange={handleFileChange} accept="image/*" />
          </div>
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Обновление...' : 'Сохранить изменения'}
        </button>

        {error && <p className="error-message">{error}</p>}
        {successMessage && <p className="success-message">{successMessage}</p>}
      </form>
    </div>
  );
};

export default EditProfile;