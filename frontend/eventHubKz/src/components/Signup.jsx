import React, { useState } from 'react';
import api from '../api';
import { useNavigate } from 'react-router-dom';

const Signup = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    role: ''
  });
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

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

    try {
      const userObj = {
        username: formData.username,
        email: formData.email,
        password: formData.password,
        role: 'USER'
      };

      const jsonBlob = new Blob([JSON.stringify(userObj)], { type: 'application/json' });

      const data = new FormData();
      data.append('user', jsonBlob);

      if (file) {
        data.append('file', file);
      }

      await api.post('/auth/signup', data, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      
      navigate('/verify');
    } catch (err) {
      console.error(err);
      setError('Ошибка регистрации. Попробуйте снова.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="signup-form">
      <h2>Регистрация</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="username"
          placeholder="Имя"
          onChange={handleChange}
          value={formData.username}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          onChange={handleChange}
          value={formData.email}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Пароль"
          onChange={handleChange}
          value={formData.password}
          required
        />
        <input
          type="file"
          onChange={handleFileChange}
          accept="image/*"
        />
        <button type="submit" disabled={loading}>
          Зарегистрироваться
        </button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </form>
    </div>
  );
};

export default Signup;
