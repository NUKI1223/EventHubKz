import axios from 'axios';


function isTokenExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const now = Math.floor(Date.now() / 1000);
    return payload.exp < now;
  } catch (error) {
    return true; 
  }
}



const api = axios.create({
  baseURL: 'http://localhost:8080',
});

api.interceptors.request.use(
  async (config) => {
    let token = localStorage.getItem('token');
    if (token) {
      if (isTokenExpired(token)) {
        try {
          const newToken = await refreshToken();
          token = newToken;
          localStorage.setItem('token', newToken);
        } catch (error) {
          console.error('Не удалось обновить токен:', error);
          localStorage.removeItem('token');
        }
      }
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);


api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const newToken = await refreshToken();
        localStorage.setItem('token', newToken);
        api.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
        return api(originalRequest);
      } catch (err) {
        console.error('Не удалось обновить токен:', err);
      }
    }
    return Promise.reject(error);
  }
);


api.get('/api/users/me')
  .then(res => {
    const user = res.data;
    localStorage.setItem('userId', user.id);
  })
  .catch(err => console.error('Ошибка получения данных пользователя', err));


export default api;
