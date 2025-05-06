import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const CreateEvent = () => {
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

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleFileChange = (e) => {
        setImage(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const data = new FormData();
        data.append('title', formData.title);
        data.append('description', formData.description);
        data.append('date', formData.date);
        data.append('location', formData.location);
        data.append('tags', formData.tags); // можно передавать через запятую
        data.append('externalLink', formData.externalLink);
        if(image){
            data.append('image', image);
        }

        try {
            const token = localStorage.getItem("token");
            await axios.post('/api/events', data, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${token}`
                }
            });
            navigate("/");
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Создать мероприятие</h2>
            <input type="text" name="title" placeholder="Название" onChange={handleChange} required />
            <textarea name="description" placeholder="Описание" onChange={handleChange} required />
            <input type="datetime-local" name="date" onChange={handleChange} required />
            <input type="text" name="location" placeholder="Место проведения" onChange={handleChange} required />
            <input type="text" name="tags" placeholder="Теги (через запятую)" onChange={handleChange} />
            <input type="url" name="externalLink" placeholder="Внешняя ссылка" onChange={handleChange} />
            <input type="file" name="image" onChange={handleFileChange} accept="image/*" />
            <button type="submit">Создать</button>
        </form>
    );
};

export default CreateEvent;
