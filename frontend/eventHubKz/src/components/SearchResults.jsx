import React, { useState, useEffect, useMemo } from 'react';
import { useLocation, Link } from 'react-router-dom';
import api from '../api';
import { formatDate } from '../utils/dateUtils';
import LikeButton from './LikeButton';
import '../css/EventList.css';

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

const SearchResults = () => {
  const query = useQuery().get('query');
  const [tab, setTab] = useState('events');
  const [eventResults, setEventResults] = useState([]);
  const [userResults, setUserResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const currentUserId = localStorage.getItem('userId');

  const [selectedTags, setSelectedTags] = useState([]);
  const [selectedCity, setSelectedCity] = useState('');
  const [onlineOnly, setOnlineOnly] = useState(false);
  const [sortOption, setSortOption] = useState('');

  // Загрузка данных
  useEffect(() => {
    const fetchResults = async () => {
      if (!query) {
        setError('Введите запрос для поиска');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        // Поиск мероприятий
        const eventRes = await api.get(`/api/search?query=${encodeURIComponent(query)}`);
        console.log('Ответ от API поиска мероприятий:', JSON.stringify(eventRes.data, null, 2));
        setEventResults(Array.isArray(eventRes.data) ? eventRes.data : []);

        // Поиск пользователей
        const userRes = await api.get(`/api/users?query=${encodeURIComponent(query)}`);
        console.log('Ответ от API поиска пользователей:', JSON.stringify(userRes.data, null, 2));
        const users = Array.isArray(userRes.data) ? userRes.data : [];
        // Локальная фильтрация, если бэкенд не фильтрует
        const filteredUsers = users.filter((user) =>
          user.username?.toLowerCase().includes(query.toLowerCase())
        );
        setUserResults(filteredUsers);
      } catch (err) {
        console.error('Ошибка поиска:', err);
        setError(err.response?.data?.message || 'Ошибка при поиске');
      } finally {
        setLoading(false);
      }
    };

    fetchResults();
  }, [query]);

  // Фильтрация и сортировка мероприятий с useMemo
  const filteredEvents = useMemo(() => {
    let filtered = [...eventResults];

    if (selectedTags.length > 0) {
      filtered = filtered.filter((event) =>
        selectedTags.every((tag) => event.tags?.includes(tag))
      );
    }
    if (selectedCity) {
      filtered = filtered.filter((event) =>
        event.location?.toLowerCase().includes(selectedCity.toLowerCase())
      );
    }
    if (onlineOnly) {
      filtered = filtered.filter((event) => event.online);
    }

    if (sortOption === 'nameAsc') {
      filtered.sort((a, b) => a.title?.localeCompare(b.title));
    } else if (sortOption === 'date') {
      filtered.sort((a, b) => new Date(a.eventDate) - new Date(b.eventDate));
    } else if (sortOption === 'likes') {
      filtered.sort((a, b) => (b.likeCount || 0) - (a.likeCount || 0));
    }

    return filtered;
  }, [eventResults, selectedTags, selectedCity, onlineOnly, sortOption]);

  if (loading) return <p className="loading">Загрузка результатов...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="search-results-container">
      <h2>Результаты поиска: {query || 'Все результаты'}</h2>

      {/* Вкладки */}
      <div className="search-tabs">
        <button
          className={tab === 'events' ? 'active' : ''}
          onClick={() => setTab('events')}
        >
          Мероприятия
        </button>
        <button
          className={tab === 'users' ? 'active' : ''}
          onClick={() => setTab('users')}
        >
          Пользователи
        </button>
      </div>

      {/* Фильтры для мероприятий */}
      {tab === 'events' && (
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
            <option value="likes">Популярность</option>
          </select>
        </div>
      )}

      {/* Теги для мероприятий */}
      {tab === 'events' && (
        <div className="tag-filter">
          <label>Тэги:</label>
          <button onClick={() => setSelectedTags([])}>Сброс</button>
          <div className="tag-list">
            {['IT', 'Hackathon', 'Java', 'Python', 'JavaScript', 'C++', 'C#', 'Ruby', 'PHP', 'DevOps'].map((tag) => (
              <button
                key={tag}
                className={selectedTags.includes(tag) ? 'selected' : ''}
                onClick={() =>
                  setSelectedTags((prev) =>
                    prev.includes(tag) ? prev.filter((t) => t !== tag) : [...prev, tag]
                  )
                }
              >
                {tag}
              </button>
            ))}
          </div>
        </div>
      )}

      {/* Результаты мероприятий */}
      {tab === 'events' && (
        <div className="events-grid">
          {filteredEvents.length > 0 ? (
            filteredEvents.map((event) => (
              <div key={event.id} className="event-card">
                {event.mainImageUrl && (
                  <img src={event.mainImageUrl} alt={event.title} className="event-image" />
                )}
                <div className="event-content">
                  <Link to={`/events/${event.id}`} className="event-title-link">
                    <h3 className="event-title">{event.title || 'Без названия'}</h3>
                  </Link>
                  <p className="event-description">{event.shortDescription || event.description || 'Нет описания'}</p>
                  <div className="tags">
                    {event.tags?.map((tag, index) => (
                      <span key={index} className="tag">{tag}</span>
                    ))}
                  </div>
                  <p className="event-date"><strong>Дата:</strong> {formatDate(event.eventDate)}</p>
                  <p className="event-location"><strong>Город:</strong> {event.location || 'Не указан'}</p>
                  <Link to={`/events/${event.id}`} className="details-button">Подробнее</Link>
                  <LikeButton eventId={event.id} currentUserId={currentUserId} />
                </div>
              </div>
            ))
          ) : (
            <p className="no-events">Нет мероприятий по вашему запросу</p>
          )}
        </div>
      )}

      {/* Результаты пользователей */}
      {tab === 'users' && (
        <div className="users-grid">
          {userResults.length > 0 ? (
            userResults.map((user) => (
              <div key={user.id} className="user-card">
                <Link to={`/profile/${user.username}`} className="user-link">
                  {user.avatarUrl ? (
                    <img src={user.avatarUrl} alt={user.username} className="user-avatar" />
                  ) : (
                    <div className="user-avatar-placeholder"></div>
                  )}
                  <h3>{user.username || 'Без имени'}</h3>
                  <p>{user.description || 'Нет описания'}</p>
                </Link>
              </div>
            ))
          ) : (
            <p className="no-users">Нет пользователей по вашему запросу</p>
          )}
        </div>
      )}
    </div>
  );
};

export default SearchResults;