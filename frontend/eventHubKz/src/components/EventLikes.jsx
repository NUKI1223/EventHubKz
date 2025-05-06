import React, { useState, useEffect } from "react";
import api from "../api";
import "../css/EventLikes.css";

function EventLikes({ eventId, currentUserId, onLikeChange, onLikedChange, onShowLikes }) {
  const [likeCount, setLikeCount] = useState(0);
  const [liked, setLiked] = useState(false);

  useEffect(() => {
    if (!eventId) return;
    const fetchLikeCount = async () => {
      try {
        const res = await api.get(`/api/like/${eventId}`);
        setLikeCount(res.data);
        if (onLikeChange) onLikeChange(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchLikeCount();
  }, [eventId, onLikeChange]);

  useEffect(() => {
    if (currentUserId && eventId) {
      const fetchUserLikes = async () => {
        try {
          const res = await api.get(`/api/like/user/${currentUserId}`);
          const likedEventIds = res.data.map(event => event.id);
          setLiked(likedEventIds.includes(eventId));
          if (onLikedChange) onLikedChange(likedEventIds.includes(eventId));
        } catch (err) {
          console.error('Ошибка получения лайков пользователя', err);
        }
      };
      fetchUserLikes();
    }
  }, [currentUserId, eventId, onLikedChange]);

  const toggleLike = async () => {
    if (!currentUserId) {
      alert('Вы должны быть авторизованы для лайка');
      return;
    }
    try {
      if (liked) {
        await api.delete(`/api/like/${eventId}`);
        setLikeCount(prev => {
          const newCount = prev - 1;
          if (onLikeChange) onLikeChange(newCount);
          return newCount;
        });
        setLiked(false);
        if (onLikedChange) onLikedChange(false);
      } else {
        await api.post(`/api/like/${eventId}`);
        setLikeCount(prev => {
          const newCount = prev + 1;
          if (onLikeChange) onLikeChange(newCount);
          return newCount;
        });
        setLiked(true);
        if (onLikedChange) onLikedChange(true);
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="event-likes">
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
        <button
          onClick={onShowLikes}
          className="toggle-likes-btn"
          disabled={likeCount === 0}
        >
          Кто лайкнул ({likeCount || 0})
        </button>
      </div>
    </div>
  );
}

export default EventLikes;