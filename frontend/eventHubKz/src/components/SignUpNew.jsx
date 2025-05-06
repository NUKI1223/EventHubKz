import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import poster from "../assets/images/signup_poster.png";
import email_icon from "../assets/images/mail_icon.png";
import password_icon from "../assets/images/password_icon.png";
import name_icon from "../assets/images/user_icon.png";
import view_icon from "../assets/images/view_icon.png";

function SignUpNew() {
    const [showPassword, setShowPassword] = useState(false);
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });
    const [file, setFile] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const togglePasswordVisibility = () => setShowPassword(prev => !prev);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
        if (!passwordRegex.test(formData.password)) {
            setError("Пароль должен содержать минимум 8 символов, заглавную и строчную буквы и хотя бы одну цифру.");
            setLoading(false);
            return;
        }

        try {
            const userObj = {
                username: formData.username,
                email: formData.email,
                password: formData.password,
                role: 'USER'
            };

            const jsonBlob = new Blob([JSON.stringify(userObj)], { type: 'application/json' });
            const data = new FormData();
            data.append('user', jsonBlob);
            if (file) {
                data.append('file', file);
            }

            await api.post('/auth/signup', data, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });

            localStorage.setItem('signupEmail', formData.email);
            
            navigate('/verification');
        } catch (err) {
            console.error(err);
        
            if (err.response) {
                const status = err.response.status;
                const message = err.response.data.message;
        
                if (status === 409) {
                    if (message.includes('User already exists')) {
                        setError('Пользователь с таким email или именем уже существует.');
                    } else {
                        setError('Конфликт данных. Проверьте форму.');
                    }
                } else {
                    setError('Ошибка регистрации. Попробуйте снова.');
                }
            } else {
                setError('Ошибка сети. Проверьте подключение.');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <section className="signup">
            <div className="signup__poster">
                <img src={poster} alt="sign up poster image" className="signup__image" />
            </div>
            <div className="signup__main">
                <h1 className="signup__create-account">Регистрация</h1>
                <form onSubmit={handleSubmit} className="signup__input-list">
                    <div className="input-item">
                        <label>Почта</label>
                        <div className="input-field">
                            <img src={email_icon} alt="email icon" className="icon" />
                            <input
                                type="email"
                                name="email"
                                placeholder="Введите почту"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>
                    <div className="input-item">
                        <label>Имя</label>
                        <div className="input-field">
                            <img src={name_icon} alt="user icon" className="icon" />
                            <input
                                type="text"
                                name="username"
                                placeholder="Введите имя"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>
                    <div className="input-item">
                        <label>Пароль</label>
                        <div className="input-field">
                            <img src={password_icon} alt="password icon" className="icon" />
                            <input
                                type={showPassword ? "text" : "password"}
                                name="password"
                                placeholder="Введите пароль"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                            <img
                                src={view_icon}
                                alt="toggle password"
                                className="toggle-password"
                                onClick={togglePasswordVisibility}
                            />
                        </div>
                    </div>
                    {error && <p className="error-text">{error}</p>}
                    <button className="signup__btn" type="submit" disabled={loading}>
                        {loading ? 'Загрузка...' : 'Создать аккаунт'}
                    </button>
                </form>
                <div className="signup__divider">- или -</div>
                <p className="signup__signin-text">
                    Уже есть аккаунт? <a href="/signin" className="signup__signin-link">Войти</a>
                </p>
            </div>
        </section>
    );
}

export default SignUpNew;
