import React from "react";
import poster from "../assets/images/mainpage_poster.png";
import worker_icon from "../assets/images/worker_icon.png";
import puzzle_icon from "../assets/images/puzzle_icon.png";
import vehicle_icon from "../assets/images/vehicle_icon.png";

function MainPage() {
    return(
        <>
            <section className="mainpage">
                <div className="mainpage__container">
                    {/* Слева контент */}
                    <div className="mainpage__content">
                    <h1 className="mainpage__title">EventHubKZ</h1>
                    <p className="mainpage__description">
                        Это платформа, созданная для поиска, отслеживания и участия в IT-событиях по всему Казахстану.
                        Мы собираем на одной площадке хакатоны, митапы, конференции и мастер-классы,
                        чтобы упростить доступ к новым знаниям, опыту и нетворкингу.
                    </p>
                    </div>

                    {/* Справа постер */}
                    <div className="mainpage__poster">
                    <img src={poster} alt="main page's poster" className="mainpage__image" />
                    </div>
                </div>

                {/* Ниже — преимущества */}
                <div className="mainpage__advantages">
                    <div className="advantage-card">
                    <img src={puzzle_icon} alt="Преимущество 1" className="advantage-icon" />
                    <h3 className="advantage-title">Полная подборка</h3>
                    <p className="advantage-text">Хакатоны, митапы, лекции, воркшопы — всё в одном месте.</p>
                    </div>
                    <div className="advantage-card">
                    <img src={vehicle_icon} alt="Преимущество 2" className="advantage-icon" />
                    <h3 className="advantage-title">Удобная навигация</h3>
                    <p className="advantage-text">Лёгкий поиск по интересам, датам и городам.</p>
                    </div>
                    <div className="advantage-card">
                    <img src={worker_icon} alt="Преимущество 3" className="advantage-icon" />
                    <h3 className="advantage-title">Карьерный рост</h3>
                    <p className="advantage-text">Участвуй в событиях, прокачивай скиллы и находи работу мечты.</p>
                    </div>
                </div>
            </section>
        </>
    )
}

export default MainPage;