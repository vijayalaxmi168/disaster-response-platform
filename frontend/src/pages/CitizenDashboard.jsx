import React, { useEffect, useState } from 'react'
import { RescueRequestAPI } from '../api.js'
import StatusBadge from '../components/StatusBadge.jsx'

export default function CitizenDashboard({ user }) {
  const [requests, setRequests] = useState([])
  const [form, setForm] = useState({ description: '', location: '' })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [loading, setLoading] = useState(false)

  const loadRequests = async () => {
    try {
      const res = await RescueRequestAPI.getByCitizen(user.id)
      setRequests(res.data)
    } catch (err) {
      setError('Could not load your requests. Is the backend running?')
    }
  }

  useEffect(() => {
    loadRequests()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    setLoading(true)
    try {
      await RescueRequestAPI.create({
        citizenId: user.id,
        citizenName: user.name,
        citizenEmail: user.email,
        description: form.description,
        location: form.location,
      })
      setForm({ description: '', location: '' })
      setSuccess('Rescue request submitted! A volunteer will be assigned soon.')
      loadRequests()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not submit request.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="container">
      <div className="card">
        <h2>Create Rescue Request</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Location</label>
            <input name="location" value={form.location} onChange={handleChange} placeholder="e.g. 12 MG Road, Bengaluru" required />
          </div>
          <div className="form-group">
            <label>Describe the emergency</label>
            <textarea name="description" rows="3" value={form.description} onChange={handleChange} required />
          </div>
          {error && <p className="error-text">{error}</p>}
          {success && <p className="success-text">{success}</p>}
          <button className="primary" type="submit" disabled={loading}>
            {loading ? 'Submitting...' : 'Submit Request'}
          </button>
        </form>
      </div>

      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h2>My Requests</h2>
          <button className="secondary" onClick={loadRequests}>Refresh</button>
        </div>
        {requests.length === 0 && <p>No requests yet.</p>}
        {requests.map((r) => (
          <div key={r.id} className="card" style={{ background: '#f9fafb' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <strong>Request #{r.id}</strong>
              <StatusBadge status={r.status} />
            </div>
            <p>{r.description}</p>
            <p style={{ fontSize: 13, color: '#6b7280' }}>📍 {r.location}</p>
            {r.volunteerName && <p style={{ fontSize: 13 }}>Volunteer assigned: {r.volunteerName}</p>}
          </div>
        ))}
      </div>
    </div>
  )
}
