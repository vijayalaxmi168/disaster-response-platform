import React, { useEffect, useState } from 'react'
import { RescueRequestAPI, VolunteerAPI } from '../api.js'
import StatusBadge from '../components/StatusBadge.jsx'

export default function VolunteerDashboard({ user }) {
  const [volunteer, setVolunteer] = useState(null)
  const [pendingRequests, setPendingRequests] = useState([])
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [assigning, setAssigning] = useState(null)

  const findMyVolunteerProfile = async () => {
    try {
      const res = await VolunteerAPI.getAll()
      const mine = res.data.find((v) => v.email === user.email)
      setVolunteer(mine || null)
    } catch (err) {
      setError('Could not reach volunteer-service.')
    }
  }

  const loadPending = async () => {
    try {
      const res = await RescueRequestAPI.getByStatus('PENDING')
      setPendingRequests(res.data)
    } catch (err) {
      setError('Could not reach rescue-request-service.')
    }
  }

  useEffect(() => {
    findMyVolunteerProfile()
    loadPending()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const createProfile = async () => {
    setError('')
    try {
      const res = await VolunteerAPI.create({ name: user.name, email: user.email, phone: user.phone, skills: 'General rescue' })
      setVolunteer(res.data)
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create volunteer profile.')
    }
  }

  const handleAssign = async (requestId) => {
    if (!volunteer) return
    setError('')
    setSuccess('')
    setAssigning(requestId)
    try {
      await VolunteerAPI.assign(volunteer.id, requestId)
      setSuccess(`You've been assigned to request #${requestId}. Check your email!`)
      loadPending()
      findMyVolunteerProfile()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not assign you to this request.')
    } finally {
      setAssigning(null)
    }
  }

  return (
    <div className="container">
      {!volunteer && (
        <div className="card">
          <h2>Set up your Volunteer Profile</h2>
          <p>We need a quick volunteer profile before you can accept rescue requests.</p>
          {error && <p className="error-text">{error}</p>}
          <button className="primary" onClick={createProfile}>Create Volunteer Profile</button>
        </div>
      )}

      {volunteer && (
        <div className="card">
          <h2>Welcome, {volunteer.name}</h2>
          <p>Status: {volunteer.available ? '🟢 Available' : '🔴 Currently on assignment'}</p>
        </div>
      )}

      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h2>Pending Rescue Requests</h2>
          <button className="secondary" onClick={loadPending}>Refresh</button>
        </div>
        {error && <p className="error-text">{error}</p>}
        {success && <p className="success-text">{success}</p>}
        {pendingRequests.length === 0 && <p>No pending requests right now.</p>}
        {pendingRequests.map((r) => (
          <div key={r.id} className="card" style={{ background: '#f9fafb' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <strong>Request #{r.id}</strong>
              <StatusBadge status={r.status} />
            </div>
            <p>{r.description}</p>
            <p style={{ fontSize: 13, color: '#6b7280' }}>📍 {r.location} — by {r.citizenName}</p>
            <button
              className="primary"
              disabled={!volunteer || !volunteer.available || assigning === r.id}
              onClick={() => handleAssign(r.id)}
            >
              {assigning === r.id ? 'Assigning...' : 'Accept this request'}
            </button>
          </div>
        ))}
      </div>
    </div>
  )
}
