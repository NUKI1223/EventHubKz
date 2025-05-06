import React, { useState, useEffect } from 'react';
import api from '../api';
import '../css/LikeButton.css';

const LikeButton = ({ eventId, currentUserId, onLikeChange, onLikedChange }) => {
  const [liked, setLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(0);
  const [showLikesModal, setShowLikesModal] = useState(false);
  const [likedUsers, setLikedUsers] = useState([]);

  // Загрузка статуса лайка
  useEffect(() => {
    if (currentUserId && eventId) {
      const fetchUserLikes = async () => {
        try {
          const res = await api.get(`/api/like/user/${currentUserId}`);
          const likedEventIds = res.data.map(event => event.id);
          setLiked(likedEventIds.includes(eventId));
        } catch (err) {
          console.error('Ошибка получения лайков пользователя', err);
        }
      };
      fetchUserLikes();
    }
  }, [currentUserId, eventId]);

  // Загрузка количества лайков
  useEffect(() => {
    if (!eventId) return;
    const fetchLikeCount = async () => {
      try {
        const res = await api.get(`/api/like/${eventId}`);
        setLikeCount(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchLikeCount();
  }, [eventId]);

  // Отдельный useEffect для вызова onLikeChange
  useEffect(() => {
    if (onLikeChange) {
      onLikeChange(likeCount);
    }
  }, [likeCount, onLikeChange]);

  // Отдельный useEffect для вызова onLikedChange
  useEffect(() => {
    if (onLikedChange) {
      onLikedChange(liked);
    }
  }, [liked, onLikedChange]);

  const toggleLike = async () => {
    if (!currentUserId) {
      alert('Вы должны быть авторизованы для лайка');
      return;
    }
    try {
      if (liked) {
        await api.delete(`/api/like/${eventId}`);
        setLikeCount(prev => prev - 1);
        setLiked(false);
      } else {
        await api.post(`/api/like/${eventId}`);
        setLikeCount(prev => prev + 1);
        setLiked(true);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const fetchLikedUsers = async () => {
    try {
      const res = await api.get(`/api/like/${eventId}/users`);
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

  return (
    <div className="like-button-container">
      <button
        className={`like-button ${liked ? 'liked' : ''}`}
        onClick={toggleLike}
        disabled={!currentUserId}
      >
        <span role="img" aria-label="heart">
          {liked ? '❤️' : '🤍'}
        </span>
      </button>
      <span
        className="like-count"
        onClick={likeCount > 0 ? fetchLikedUsers : undefined}
        style={{ cursor: likeCount > 0 ? 'pointer' : 'default' }}
      >
        {likeCount}
      </span>

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
                    <span className="liked-user-name">{user.username}</span>
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

export default LikeButton;