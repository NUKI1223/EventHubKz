import React, { useState, useEffect } from 'react';
import api from '../api';
import { formatDate } from '../utils/dateUtils';
import '../css/AdminDashboard.css';

const AdminDashboard = () => {
  const [eventRequests, setEventRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [currentAction, setCurrentAction] = useState({ id: null, status: '', adminComment: '' });

  useEffect(() => {
    const fetchEventRequests = async () => {
      try {
        const res = await api.get('/api/admin/all');
        setEventRequests(res.data);
      } catch (err) {
        console.error(err);
        setError('Ошибка при загрузке заявок на мероприятия');
      } finally {
        setLoading(false);
      }
    };
    fetchEventRequests();
  }, []);

  const updateRequest = async () => {
    const { id, status, adminComment } = currentAction;
    try {
      const payload = { status, adminComment };
      await api.put(`/api/admin/${id}/update`, payload);
      setEventRequests((prev) =>
        prev.map((req) => (req.id === id ? { ...req, status, adminComment } : req))
      );
      setShowModal(false);
    } catch (err) {
      console.error(err);
      alert('Ошибка при обновлении заявки');
    }
  };

  const openModal = (id, status, adminComment) => {
    setCurrentAction({ id, status, adminComment });
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setCurrentAction({ id: null, status: '', adminComment: '' });
  };

  if (loading) return <p className="loading">Загрузка...</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <div className="admin-dashboard">
      <h2>Заявки на мероприятия</h2>
      {eventRequests.length > 0 ? (
        <div className="event-requests-list">
          {eventRequests.map((request) => (
            <div key={request.id} className="event-request-card">
              <h3>{request.title}</h3>
              <p className="short-description">{request.shortDescription}</p>
              <p className="full-description">{request.fullDescription}</p>

              <div className="tags">
                {request.tags.map((tag, index) => (
                  <span key={index} className="tag">{tag}</span>
                ))}
              </div>

              <p className="location">Локация: {request.location}</p>
              <p className={`event-type ${request.online ? 'online' : 'offline'}`}>
                Тип: {request.online ? 'Онлайн' : 'Оффлайн'}
              </p>
              <p className="event-date">Дата: {formatDate(request.eventDate)}</p>
              <p className="deadline">Дедлайн регистрации: {formatDate(request.registrationDeadline)}</p>

              {request.mainImageUrl && (
                <img className="event-image" src={request.mainImageUrl} alt="Изображение мероприятия" />
              )}

              {request.externalLink && (
                <p>
                  Внешняя ссылка: <a href={request.externalLink} target="_blank" rel="noopener noreferrer">
                    {request.externalLink}
                  </a>
                </p>
              )}

              <p className="requester-email">Email заявителя: {request.requesterEmail}</p>

              <p className={`status ${request.status.toLowerCase()}`}>
                Статус: {request.status}
              </p>

              <textarea
                className="admin-comment"
                placeholder="Комментарий администратора"
                value={request.adminComment || ''}
                onChange={(e) => {
                  const newComment = e.target.value;
                  setEventRequests((prev) =>
                    prev.map((req) => (req.id === request.id ? { ...req, adminComment: newComment } : req))
                  );
                }}
              />

              <div className="action-buttons">
                <button
                  className="approve-button"
                  onClick={() => openModal(request.id, 'APPROVED', request.adminComment)}
                >
                  Одобрить
                </button>
                <button
                  className="reject-button"
                  onClick={() => openModal(request.id, 'REJECTED', request.adminComment)}
                >
                  Отклонить
                </button>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <p className="no-requests">Нет заявок</p>
      )}

      {showModal && (
        <div className="modal">
          <div className="modal-content">
            <p>
              Вы уверены, что хотите {currentAction.status === 'APPROVED' ? 'одобрить' : 'отклонить'} эту заявку?
            </p>
            <div className="modal-actions">
              <button className="confirm-button" onClick={updateRequest}>
                Подтвердить
              </button>
              <button className="cancel-button" onClick={closeModal}>
                Отмена
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
