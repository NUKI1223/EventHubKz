import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import poster from "../assets/images/signin_poster.png";
import email_icon from "../assets/images/mail_icon.png";
import password_icon from "../assets/images/password_icon.png";
import view_icon from "../assets/images/view_icon.png";

function SignIn() {
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [formData, setFormData] = useState({ email: '', password: '' });
    const [error, setError] = useState('');

    const togglePasswordVisibility = () => setShowPassword(prev => !prev);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const res = await api.post('/auth/login', formData);
            localStorage.setItem('token', res.data.token);
            navigate('/');
        } catch (err) {
            console.error(err);
            const msg = err.response?.data?.message || 'Неверный email или пароль';
            setError(msg);
        }
    };

    return (
        <section className="signin">
            <div className="signin__main">
                <h1 className="signin__welcome">С возвращением!!</h1>
                <form onSubmit={handleSubmit}>
                    <ul className="signup__input-list">
                        <li className="input-item">
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
                        </li>
                        <li className="input-item">
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
                        </li>
                    </ul>
                    <button type="submit" className="signin__btn">Войти</button>
                    {error && <p className="error-text">{error}</p>}
                </form>
                <div className="signin__divider">- или -</div>
                <p className="signin__signup-text">
                    Нет аккаунта? <a href="/signupnew" className="signup__signin-link">Зарегистрироваться</a>
                </p>
            </div>
            <div className="signin__poster">
                <img src={poster} alt="sign in poster image" className="signin__image" />
            </div>
        </section>
    );
}

export default SignIn;