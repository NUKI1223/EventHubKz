import React, { useRef, useEffect } from 'react';
import { formatDate } from '../utils/dateUtils';
import '../css/NotificationsDropdown.css';
import api from '../api';

const NotificationsDropdown = ({ notifications, onClose, username, onUpdate }) => {
  const dropdownRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
        onClose();
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [onClose]);

  const markAsRead = async (id) => {
    try {
      await api.put(`/api/notifications/${id}`);
      onUpdate(); // Обновить список уведомлений
    } catch (err) {
      console.error('Ошибка при обновлении статуса уведомления:', err);
    }
  };

  const deleteOne = async (id) => {
    try {
      await api.delete(`/api/notifications/${id}`);
      onUpdate(); // Перезагрузить список уведомлений
    } catch (err) {
      console.error('Ошибка удаления уведомления:', err);
    }
  };

  const deleteAll = async () => {
    try {
      await api.delete('/api/notifications/all', { params: { username } });
      onUpdate();
    } catch (err) {
      console.error('Ошибка удаления всех уведомлений:', err);
    }
  };

  return (
    <div className="notifications-dropdown" ref={dropdownRef}>
      <div className="notifications-header">
        Уведомления
        {notifications.length > 0 && (
          <button className="delete-all-btn" onClick={deleteAll}>
            Удалить все
          </button>
        )}
      </div>
      <div className="notifications-list">
        {notifications.length === 0 && (
          <p className="no-notifications">Нет уведомлений</p>
        )}
        {notifications.map((n) => (
          <div key={n.id} className="notification-item">
            <p className="notification-message">{n.message}</p>
            {n.link && (
              <a
                href={n.link}
                className="notification-link"
                onClick={() => markAsRead(n.id)}
                target="_blank"
                rel="noopener noreferrer"
              >
                Подробнее
              </a>
            )}
            <p className="notification-meta">{formatDate(n.createdAt)}</p>
            <p className={`notification-status ${n.read ? 'read' : 'unread'}`}>
              {n.read ? 'Прочитано' : 'Непрочитано'}
            </p>
            <button className="delete-one-btn" onClick={() => deleteOne(n.id)}>
              ×
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default NotificationsDropdown;
