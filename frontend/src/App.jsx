import React, { useState } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './components/Navbar.jsx'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import CitizenDashboard from './pages/CitizenDashboard.jsx'
import VolunteerDashboard from './pages/VolunteerDashboard.jsx'
import ShelterDashboard from './pages/ShelterDashboard.jsx'

function getStoredUser() {
  const raw = localStorage.getItem('drp_user')
  return raw ? JSON.parse(raw) : null
}

export default function App() {
  const [user, setUser] = useState(getStoredUser())

  const handleLogin = (u) => {
    localStorage.setItem('drp_user', JSON.stringify(u))
    setUser(u)
  }

  const handleLogout = () => {
    localStorage.removeItem('drp_user')
    setUser(null)
  }

  const dashboardPathForRole = (role) => {
    if (role === 'CITIZEN') return '/citizen'
    if (role === 'VOLUNTEER') return '/volunteer'
    if (role === 'ADMIN') return '/admin'
    return '/login'
  }

  return (
    <>
      <Navbar user={user} onLogout={handleLogout} />
      <Routes>
        <Route
          path="/login"
          element={user ? <Navigate to={dashboardPathForRole(user.role)} /> : <Login onLogin={handleLogin} />}
        />
        <Route
          path="/register"
          element={user ? <Navigate to={dashboardPathForRole(user.role)} /> : <Register onRegistered={handleLogin} />}
        />
        <Route
          path="/citizen"
          element={user && user.role === 'CITIZEN' ? <CitizenDashboard user={user} /> : <Navigate to="/login" />}
        />
        <Route
          path="/volunteer"
          element={user && user.role === 'VOLUNTEER' ? <VolunteerDashboard user={user} /> : <Navigate to="/login" />}
        />
        <Route
          path="/admin"
          element={user && user.role === 'ADMIN' ? <ShelterDashboard user={user} /> : <Navigate to="/login" />}
        />
        <Route path="*" element={<Navigate to={user ? dashboardPathForRole(user.role) : '/login'} />} />
      </Routes>
    </>
  )
}
