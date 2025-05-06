import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';
import { formatDate } from '../utils/dateUtils';
import '../css/EventList.css';
import LikeButton from './LikeButton';
import {jwtDecode} from 'jwt-decode';

const EventList = () => {
  const [events, setEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const currentUserId = localStorage.getItem('userId');
  const token = localStorage.getItem('token');
  let isAdmin = false;

  if (token) {
    try {
      const decoded = jwtDecode(token);
      isAdmin = decoded.role === 'ADMIN';
    } catch (err) {
      console.error('Ошибка декодирования токена:', err);
    }
  }

  const [selectedTags, setSelectedTags] = useState([]);
  const [selectedCity, setSelectedCity] = useState('');
  const [onlineOnly, setOnlineOnly] = useState(false);
  const [sortOption, setSortOption] = useState('');

  const [availableTags, setAvailableTags] = useState([]);
  const [showAllTags, setShowAllTags] = useState(false);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const res = await api.get('/api/events');
        setEvents(res.data);
        setFilteredEvents(res.data);
      } catch (err) {
        console.error(err);
        setError('Ошибка при загрузке мероприятий');
      } finally {
        setLoading(false);
      }
    };
    fetchEvents();
  }, []);

  useEffect(() => {
    const fetchTags = async () => {
      try {
        const res = await api.get('/api/tags');
        const tags = res.data.map(tag => tag.name);
        setAvailableTags(tags);
      } catch (err) {
        console.error(err);
      }
    };
    fetchTags();
  }, []);

  useEffect(() => {
    let filtered = [...events];
  
    if (selectedTags.length > 0) {
      filtered = filtered.filter(event =>
        selectedTags.every(tag => event.tags.includes(tag))
      );
    }
  
    if (selectedCity) {
      filtered = filtered.filter(event =>
        event.location && event.location.toLowerCase().includes(selectedCity.toLowerCase())
      );
    }
  
    if (onlineOnly) {
      filtered = filtered.filter(event => event.online === true || event.online === "true");
    }
  
    if (sortOption === 'nameAsc') {
      filtered.sort((a, b) => a.title.localeCompare(b.title));
    } else if (sortOption === 'date') {
      filtered.sort((a, b) => Date.parse(a.eventDate) - Date.parse(b.eventDate));
    } else if (sortOption === 'likes') {
      filtered.sort((a, b) => Number(b.likeCount) - Number(a.likeCount));
    }
  
    setFilteredEvents(filtered);
  }, [selectedTags, selectedCity, onlineOnly, sortOption, events]);
  

  const handleDelete = async (id) => {
    if (!window.confirm('Вы уверены, что хотите удалить это мероприятие?')) return;
    try {
      await api.delete(`/api/events/${id}`);
      setEvents((prev) => prev.filter((event) => event.id !== id));
      setFilteredEvents((prev) => prev.filter((event) => event.id !== id));
    } catch (err) {
      console.error('Ошибка при удалении мероприятия:', err);
      alert('Не удалось удалить мероприятие');
    }
  };

  if (loading) return <p className="loading">Загрузка...</p>;
  if (error) return <p className="error">{error}</p>;

  const tagsToShow = showAllTags ? availableTags : availableTags.slice(0, 5);

  return (
    <div className="events-container">
      <h2>Список мероприятий</h2>
      

      <div className="filters">
        <input
          type="text"
          placeholder="Город"
          value={selectedCity}
          onChange={(e) => setSelectedCity(e.target.value)}
        />
        <label>
          Онлайн:
          <input
            type="checkbox"
            checked={onlineOnly}
            onChange={() => setOnlineOnly(!onlineOnly)}
          />
        </label>
        <select value={sortOption} onChange={(e) => setSortOption(e.target.value)}>
          <option value="">Сортировка</option>
          <option value="nameAsc">Название (А-Я)</option>
          <option value="date">Дата</option>
          <option value="likes">Лайки</option>
        </select>
      </div>

      <div className="tag-filter">
        <label>Тэги:</label>
        <button className="reset-button" onClick={() => setSelectedTags([])}>Сброс</button>
        <div className="tag-list">
          {tagsToShow.map(tag => (
            <button
              key={tag}
              className={selectedTags.includes(tag) ? 'selected' : '' }
              onClick={() =>
                setSelectedTags(prev =>
                  prev.includes(tag) ? prev.filter(t => t !== tag) : [...prev, tag]
                )
              }
            >
              {tag}
            </button>
          ))}
          {availableTags.length > 5 && (
            <button className="more-button" onClick={() => setShowAllTags(!showAllTags)}>
              {showAllTags ? 'Свернуть' : 'Еще'}
            </button>
          )}
        </div>
      </div>

      <div className="events-grid">
        {filteredEvents.length > 0 ? (
          filteredEvents.map(event => (
            <div key={event.id} className="event-card">
              {event.mainImageUrl && (
                <img src={event.mainImageUrl} alt={event.title} className="event-image" />
              )}
              <div className="event-content">
                <Link to={`/events/${event.id}`} className="event-title-link">
                  <h3 className="event-title">{event.title}</h3>
                </Link>
                <p className="event-description">{event.shortDescription}</p>
                <div className="tags">
                  {event.tags.map(tag => (
                    <span key={tag} className="tag">{tag}</span>
                  ))}
                </div>
                <p className="event-date"><strong>Дата:</strong> {formatDate(event.eventDate)}</p>
                <p className="event-location"><strong>Город:</strong> {event.location}</p>
                <div className="event-actions">
                  <div className="action-buttons">
                    <Link to={`/events/${event.id}`} className="details-button">Подробнее</Link>
                    <LikeButton eventId={event.id} currentUserId={currentUserId} />
                  </div>
                  {isAdmin && (
                    <button
                      className="delete-event-btn"
                      onClick={() => handleDelete(event.id)}
                    >
                      Удалить
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))
        ) : (
          <p className="no-events">Нет мероприятий по выбранным параметрам</p>
        )}
      </div>
    </div>
  );
};

export default EventList;