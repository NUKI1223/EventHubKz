import React, { useState } from 'react';
import api from '../api';
import TagSelector from './TagSelector';
import { formatDate } from '../utils/dateUtils';

const EventRequestForm = () => {
  const [formData, setFormData] = useState({
    title: '',
    shortDescription: '',
    fullDescription: '',
    location: '',
    online: false,
    eventDate: '',
    registrationDeadline: '',
    externalLink: '',
    requesterEmail: ''
  });
  const [selectedTags, setSelectedTags] = useState([]);
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const eventRequestObj = {
        title: formData.title,
        shortDescription: formData.shortDescription,
        fullDescription: formData.fullDescription,
        tags: selectedTags,
        location: formData.location,
        online: formData.online,
        eventDate: formData.eventDate,
        registrationDeadline: formData.registrationDeadline,
        externalLink: formData.externalLink,
        requesterEmail: formData.requesterEmail
      };

      const jsonBlob = new Blob([JSON.stringify(eventRequestObj)], { type: 'application/json' });

      const data = new FormData();
      data.append('eventRequest', jsonBlob);
      if (file) {
        data.append('file', file);
      }

      await api.post('/api/users/eventRequest', data, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });

      alert('Заявка успешно отправлена');

      // Сбрасываем состояние формы
      setFormData({
        title: '',
        shortDescription: '',
        fullDescription: '',
        location: '',
        online: false,
        eventDate: '',
        registrationDeadline: '',
        externalLink: '',
        requesterEmail: ''
      });
      setSelectedTags([]);
      setFile(null);
    } catch (err) {
      console.error(err);
      setError('Ошибка при отправке заявки');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="event-request-form">
      <h2>Создать заявку на мероприятие</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="title"
          placeholder="Название"
          value={formData.title}
          onChange={handleChange}
          required
        />
        <textarea
          name="shortDescription"
          placeholder="Краткое описание"
          value={formData.shortDescription}
          onChange={handleChange}
          required
        />
        <textarea
          name="fullDescription"
          placeholder="Полное описание"
          value={formData.fullDescription}
          onChange={handleChange}
          required
        />
        <TagSelector selectedTags={selectedTags} onChange={setSelectedTags} />
        <input
          type="text"
          name="location"
          placeholder="Место проведения"
          value={formData.location}
          onChange={handleChange}
          required
        />
        <label>
          Онлайн мероприятие:
          <input
            type="checkbox"
            name="online"
            checked={formData.online}
            onChange={handleChange}
          />
        </label>
        <p>Дата проведения:</p>
        <input
          type="datetime-local"
          name="eventDate"
          value={formData.eventDate}
          onChange={handleChange}
          required
        />
        <p>Дедлайн регистрации:</p>
        <input
          type="datetime-local"
          name="registrationDeadline"
          value={formData.registrationDeadline}
          onChange={handleChange}
          required
        />
        <input
          type="url"
          name="externalLink"
          placeholder="Внешняя ссылка"
          value={formData.externalLink}
          onChange={handleChange}
        />
        <input
          type="email"
          name="requesterEmail"
          placeholder="Ваш email"
          value={formData.requesterEmail}
          onChange={handleChange}
          required
        />
        <input
          type="file"
          onChange={handleFileChange}
          accept="image/*"
        />
        <button type="submit" disabled={loading}>
          Отправить заявку
        </button>
        {error && <p style={{ color: 'red' }}>{error}</p>}
      </form>
    </div>
  );
};

export default EventRequestForm;
