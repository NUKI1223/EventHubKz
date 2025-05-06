import React from 'react';

const Support = () => {
    return (
        <div className="support-container">
            <h1>Поддержка</h1>
            <section className="faq">
                <h2>Часто задаваемые вопросы</h2>
                <ul>
                    <li>
                        <strong>Как создать мероприятие?</strong>
                        <p>Чтобы создать мероприятие, перейдите на страницу "Создать заявку" и заполните форму.</p>
                    </li>
                    <li>
                        <strong>Как изменить пароль?</strong>
                        <p>Чтобы изменить пароль, перейдите в настройки профиля и выберите "Сменить пароль".</p>
                    </li>
                </ul>
            </section>
            <section className="contact-form">
                <h2>Связаться с нами</h2>
                <div>
                    <label htmlFor="name">Имя:</label>
                    <input type="text" id="name" name="name" required />

                    <label htmlFor="email">Email:</label>
                    <input type="email" id="email" name="email" required />

                    <label htmlFor="message">Сообщение:</label>
                    <textarea id="message" name="message" rows="5" required></textarea>

                    <button type="button">Отправить</button>
                </div>
            </section>
            <section className="contact-info">
                <h2>Контактная информация</h2>
                <p>Email: support@example.com</p>
                <p>Телефон: +7 (123) 456-7890</p>
            </section>
        </div>
    );
};

export default Support;