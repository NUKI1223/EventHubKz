import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';

const UpdateEvent = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        date: '',
        location: '',
        tags: '',
        externalLink: ''
    });
    const [image, setImage] = useState(null);

    useEffect(() => {
        const fetchEvent = async () => {
            try {
                const res = await axios.get(`/api/events/${id}`);
                const event = res.data;
                setFormData({
                    title: event.title,
                    description: event.description,
                    date: event.date,
                    location: event.location,
                    tags: event.tags.join(', '),
                    externalLink: event.externalLink || ''
                });
            } catch (err) {
                console.error(err);
            }
        };
        fetchEvent();
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleFileChange = (e) => {
        setImage(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            await axios.put(`/api/events/${id}`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            if(image) {
                const imageData = new FormData();
                imageData.append('image', image);
                await axios.post(`/api/events/${id}/update-image`, imageData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        Authorization: `Bearer ${token}`
                    }
                });
            }
            navigate(`/events/${id}`);
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Обновить мероприятие</h2>
            <input
                type="text"
                name="title"
                placeholder="Название"
                value={formData.title}
                onChange={handleChange}
                required
            />
            <textarea
                name="description"
                placeholder="Описание"
                value={formData.description}
                onChange={handleChange}
                required
            />
            <input
                type="datetime-local"
                name="date"
                value={formData.date}
                onChange={handleChange}
                required
            />
            <input
                type="text"
                name="location"
                placeholder="Место проведения"
                value={formData.location}
                onChange={handleChange}
                required
            />
            <input
                type="text"
                name="tags"
                placeholder="Теги (через запятую)"
                value={formData.tags}
                onChange={handleChange}
            />
            <input
                type="url"
                name="externalLink"
                placeholder="Внешняя ссылка"
                value={formData.externalLink}
                onChange={handleChange}
            />
            <input type="file" name="image" onChange={handleFileChange} accept="image/*" />
            <button type="submit">Обновить</button>
        </form>
    );
};

export default UpdateEvent;
