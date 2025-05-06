import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api';
import { jwtDecode } from 'jwt-decode';
import { formatDate } from '../utils/dateUtils';
import '../css/EventDetail.css';
import EventLikes from './EventLikes';
import { Link } from 'react-router-dom';

const EventDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [likeCount, setLikeCount] = useState(0);
  const [liked, setLiked] = useState(false);
  const [showLikesModal, setShowLikesModal] = useState(false);
  const [likedUsers, setLikedUsers] = useState([]);
  const currentUserId = localStorage.getItem('userId');
  let isAdmin = false;
  const token = localStorage.getItem('token');

  if (token) {
    try {
      const decoded = jwtDecode(token);
      isAdmin = decoded.role === 'ADMIN';
    } catch (err) {
      console.error('Ошибка декодирования токена:', err);
    }
  }

  useEffect(() => {
    const fetchEvent = async () => {
      try {
        const res = await api.get(`/api/events/${id}`);
        setEvent(res.data);

        const likeCountRes = await api.get(`/api/like/${id}`);
        setLikeCount(likeCountRes.data);

        let liked = false;
        if (currentUserId) {
          const userLikesRes = await api.get(`/api/like/user/${currentUserId}`);
          const likedEventIds = userLikesRes.data.map(e => e.id);
          liked = likedEventIds.includes(Number(id));
        }
        setLiked(liked);
      } catch (err) {
        console.error(err);
        setError('Ошибка при загрузке мероприятия');
      } finally {
        setLoading(false);
      }
    };
    fetchEvent();
  }, [id, currentUserId]);

  const fetchLikedUsers = async () => {
    try {
      const res = await api.get(`/api/like/${id}/users`);
      setLikedUsers(res.data);
      setShowLikesModal(true);
    } catch (err) {
      console.error('Ошибка получения пользователей, лайкнувших мероприятие:', err);
      alert('Не удалось загрузить список пользователей');
    }
  };

  const closeModal = () => {
    setShowLikesModal(false);
    setLikedUsers([]);
  };

  if (loading) return <p>Загрузка...</p>;
  if (error) return <p>{error}</p>;
  if (!event) return <p>Мероприятие не найдено</p>;

  return (
    <div className="event-detail-container">
      <div className="event-detail-header">
        {event.mainImageUrl && (
          <img src={event.mainImageUrl} alt={event.title} className="event-detail-image" />
        )}
        <h2 className="event-detail-title">{event.title}</h2>
      </div>
      <div className="event-detail-content">
        <div className="event-detail-info">
          <p><strong>Дата мероприятия:</strong> {formatDate(event.eventDate)}</p>
          <p><strong>Дедлайн регистрации:</strong> {formatDate(event.registrationDeadline)}</p>
          <p><strong>Формат:</strong> {event.online ? 'Онлайн' : 'Оффлайн'}</p>
          {!event.online && event.location && (
            <p><strong>Место проведения:</strong> {event.location}</p>
          )}
          {event.tags && event.tags.length > 0 && (
            <p><strong>Тэги:</strong> {event.tags.join(', ')}</p>
          )}
          <p><strong>Организатор:</strong> {event.organizerEmail}</p>
        </div>
        <div className="event-detail-description">
          <h3>Описание мероприятия</h3>
          <p>{event.fullDescription}</p>
          {event.externalLink && (
            <p>
              <a href={event.externalLink} target="_blank" rel="noopener noreferrer">
                Перейти на сайт мероприятия
              </a>
            </p>
          )}
          <div className="event-actions">
            <EventLikes
              eventId={event.id}
              currentUserId={currentUserId}
              onLikeChange={setLikeCount}
              onLikedChange={setLiked}
              onShowLikes={fetchLikedUsers} // Передаем функцию для открытия модального окна
            />
          </div>
        </div>
      </div>

      {showLikesModal && (
        <div className="likes-modal-overlay">
          <div className="likes-modal">
            <h3>Кто лайкнул это мероприятие</h3>
            <button className="modal-close-btn" onClick={closeModal}>✖</button>
            <div className="liked-users-list">
              {likedUsers.length > 0 ? (
                likedUsers.map(user => (
                  <div key={user.id} className="liked-user-card">
                    <div className="liked-user-avatar">
                      {user.avatarUrl ? (
                        <img src={user.avatarUrl} alt={user.username} className="user-avatar" />
                      ) : (
                        <div className="user-avatar-placeholder"></div>
                      )}
                    </div>
                    <Link to={`/profile/${user.username}`} className="liked-user-name">
                      {user.username}
                    </Link>
                  </div>
                ))
              ) : (
                <p>Никто еще не лайкнул это мероприятие.</p>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default EventDetail;