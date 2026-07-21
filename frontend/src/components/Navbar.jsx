import React from 'react'
import { Link } from 'react-router-dom'

export default function Navbar({ user, onLogout }) {
  return (
    <div className="navbar">
      <h1>🚨 Disaster Response Platform</h1>
      <div className="links">
        {!user && <Link to="/login">Login</Link>}
        {!user && <Link to="/register">Register</Link>}
        {user && <span style={{ marginRight: 12 }}>{user.name} ({user.role})</span>}
        {user && <button onClick={onLogout}>Logout</button>}
      </div>
    </div>
  )
}
