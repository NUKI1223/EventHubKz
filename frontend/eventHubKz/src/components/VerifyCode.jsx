import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import '../css/VerifyCode.css';

const VerifyCode = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [verificationCode, setVerificationCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const data = {
        email,
        verificationCode
      };
      const response = await api.post('/auth/verify', data);
      console.log(response.data);
      alert('Код успешно подтвержден!');
      navigate('/login');
    } catch (err) {
      console.error(err);
      setError('Ошибка проверки кода. Проверьте введенные данные.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="verify-code-form">
      <h2>Проверка кода верификации</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="email"
          name="email"
          placeholder="Введите ваш email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="text"
          name="verificationCode"
          placeholder="Введите код верификации"
          value={verificationCode}
          onChange={(e) => setVerificationCode(e.target.value)}
          required
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Проверка...' : 'Проверить код'}
        </button>
      </form>
      {error && <p className="error-text">{error}</p>}
    </div>
  );
};

export default VerifyCode;
