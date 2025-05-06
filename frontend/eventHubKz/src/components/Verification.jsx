import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import poster from "../assets/images/signup_poster.png";
import email_icon from "../assets/images/password_icon.png";

function Verification() {
    const navigate = useNavigate();
    const [verificationCode, setVerificationCode] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const email = localStorage.getItem('signupEmail');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const data = { email, verificationCode };
            await api.post('/auth/verify', data);
            alert('Код успешно подтвержден!');
            navigate('/signin');
        } catch (err) {
            console.error(err);
            setError('Ошибка проверки кода. Проверьте введенные данные.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <section className="verification">
            <div className="verification__poster">
                <img src={poster} alt="verification poster image" className="verification__image" />
            </div>
            <div className="verification__main">
                <h1 className="verification__title">Верификация почты</h1>
                <form onSubmit={handleSubmit} className="signup__input-list">
                    <li className="input-item">
                        <label>Код верификации</label>
                        <div className="input-field">
                            <img src={email_icon} alt="email icon" className="icon" />
                            <input
                                type="text"
                                placeholder="Введите код"
                                value={verificationCode}
                                onChange={(e) => setVerificationCode(e.target.value)}
                                required
                            />
                        </div>
                    </li>
                    {error && <p className="error-text">{error}</p>}
                    <button className="verification__btn" type="submit" disabled={loading}>
                        {loading ? 'Проверка...' : 'Проверить код'}
                    </button>
                </form>
            </div>
        </section>
    );
}

export default Verification;
