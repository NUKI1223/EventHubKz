import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import "../src/css/typography.css"
import "../src/css/SignUpNew.css"
import "../src/css/SignIn.css"
import "../src/css/Verification.css"
import "../src/css/Header.css"
import "../src/css/MainPage.css"
import "../src/css/EventRequestForm.css"
import "../src/css/Support.css"

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
