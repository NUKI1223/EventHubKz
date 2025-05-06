import React, { useState, useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import api from '../api';
import { formatDate } from '../utils/dateUtils';
import '../css/LikedEvents.css';

const LikedEvents = () => {
  const { username: routeUsername } = useParams();
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchLikedEvents = async () => {
      try {
        let userId;
        if (routeUsername) {
          const userRes = await api.get(`/api/users/${routeUsername}`);
          userId = userRes.data.id;
        } else {
          const meRes = await api.get('/api/users/me');
          userId = meRes.data.id;
        }
        const res = await api.get(`/api/like/user/${userId}`);
        setEvents(res.data);
      } catch (err) {
        console.error(err);
        setError('Ошибка при загрузке лайкнутых мероприятий');
      } finally {
        setLoading(false);
      }
    };
    fetchLikedEvents();
  }, [routeUsername]);

  if (loading) return <p className="loading">Загрузка...</p>;
  if (error) return <p className="error">{error}</p>;
  if (events.length === 0) return <p className="no-events">Нет лайкнутых мероприятий</p>;

  return (
    <div className="liked-events-container">
      <h2>Лайкнутые мероприятия {routeUsername ? `пользователя ${routeUsername}` : ''}</h2>
      <div className="liked-events-grid">
        {events.map(event => (
          <div key={event.id} className="liked-event-card">
            {event.mainImageUrl && (
              <img src={event.mainImageUrl} alt={event.title} className="liked-event-image" />
            )}
            <div className="liked-event-content">
              <Link to={`/events/${event.id}`} className="liked-event-title-link">
                <h3 className="liked-event-title">{event.title}</h3>
              </Link>
              <p className="liked-event-description">
                <strong>Краткое описание:</strong> {event.shortDescription}
              </p>
              <p className="liked-event-date">
                <strong>Дата:</strong> {formatDate(event.eventDate)}
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default LikedEvents;
