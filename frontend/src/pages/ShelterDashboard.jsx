import React, { useEffect, useState } from 'react'
import { ShelterAPI, RescueRequestAPI } from '../api.js'
import StatusBadge from '../components/StatusBadge.jsx'

export default function ShelterDashboard() {
  const [shelters, setShelters] = useState([])
  const [requests, setRequests] = useState([])
  const [shelterForm, setShelterForm] = useState({ name: '', location: '', totalCapacity: '' })
  const [assignForm, setAssignForm] = useState({})
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const loadShelters = async () => {
    try {
      const res = await ShelterAPI.getAll()
      setShelters(res.data)
    } catch (err) {
      setError('Could not reach shelter-service.')
    }
  }

  const loadRequests = async () => {
    try {
      const res = await RescueRequestAPI.getAll()
      setRequests(res.data)
    } catch (err) {
      setError('Could not reach rescue-request-service.')
    }
  }

  useEffect(() => {
    loadShelters()
    loadRequests()
  }, [])

  const handleShelterFormChange = (e) => setShelterForm({ ...shelterForm, [e.target.name]: e.target.value })

  const handleAddShelter = async (e) => {
    e.preventDefault()
    setError('')
    try {
      await ShelterAPI.create({
        name: shelterForm.name,
        location: shelterForm.location,
        totalCapacity: Number(shelterForm.totalCapacity),
      })
      setShelterForm({ name: '', location: '', totalCapacity: '' })
      loadShelters()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not add shelter.')
    }
  }

  const handleAssignChange = (shelterId, field, value) => {
    setAssignForm({ ...assignForm, [shelterId]: { ...assignForm[shelterId], [field]: value } })
  }

  const handleAssignPeople = async (shelterId) => {
    setError('')
    setSuccess('')
    const data = assignForm[shelterId] || {}
    try {
      await ShelterAPI.assignPeople(shelterId, {
        personName: data.personName,
        numberOfPeople: Number(data.numberOfPeople),
      })
      setSuccess(`Assigned ${data.numberOfPeople} people to shelter #${shelterId}.`)
      setAssignForm({ ...assignForm, [shelterId]: {} })
      loadShelters()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not assign people to shelter.')
    }
  }

  const markCompleted = async (id) => {
    setError('')
    try {
      await RescueRequestAPI.updateStatus(id, 'COMPLETED')
      loadRequests()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not update status.')
    }
  }

  return (
    <div className="container">
      <div className="card">
        <h2>Add Shelter</h2>
        <form onSubmit={handleAddShelter} className="grid-2">
          <div className="form-group">
            <label>Name</label>
            <input name="name" value={shelterForm.name} onChange={handleShelterFormChange} required />
          </div>
          <div className="form-group">
            <label>Location</label>
            <input name="location" value={shelterForm.location} onChange={handleShelterFormChange} required />
          </div>
          <div className="form-group">
            <label>Total Capacity</label>
            <input name="totalCapacity" type="number" min="1" value={shelterForm.totalCapacity} onChange={handleShelterFormChange} required />
          </div>
          <div style={{ display: 'flex', alignItems: 'flex-end' }}>
            <button className="primary" type="submit">Add Shelter</button>
          </div>
        </form>
        {error && <p className="error-text">{error}</p>}
        {success && <p className="success-text">{success}</p>}
      </div>

      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h2>Shelters</h2>
          <button className="secondary" onClick={loadShelters}>Refresh</button>
        </div>
        {shelters.length === 0 && <p>No shelters added yet.</p>}
        {shelters.map((s) => (
          <div key={s.id} className="card" style={{ background: '#f9fafb' }}>
            <strong>{s.name}</strong> — 📍 {s.location}
            <p style={{ fontSize: 13, color: '#6b7280' }}>
              Available capacity: {s.availableCapacity} / {s.totalCapacity}
            </p>
            <div className="grid-2">
              <input
                placeholder="Person / group name"
                value={assignForm[s.id]?.personName || ''}
                onChange={(e) => handleAssignChange(s.id, 'personName', e.target.value)}
              />
              <input
                type="number"
                min="1"
                placeholder="Number of people"
                value={assignForm[s.id]?.numberOfPeople || ''}
                onChange={(e) => handleAssignChange(s.id, 'numberOfPeople', e.target.value)}
              />
            </div>
            <button className="secondary" style={{ marginTop: 8 }} onClick={() => handleAssignPeople(s.id)}>
              Assign to this shelter
            </button>
          </div>
        ))}
      </div>

      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h2>All Rescue Requests</h2>
          <button className="secondary" onClick={loadRequests}>Refresh</button>
        </div>
        {requests.map((r) => (
          <div key={r.id} className="card" style={{ background: '#f9fafb' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <strong>Request #{r.id} — {r.citizenName}</strong>
              <StatusBadge status={r.status} />
            </div>
            <p>{r.description}</p>
            <p style={{ fontSize: 13, color: '#6b7280' }}>📍 {r.location}</p>
            {r.volunteerName && <p style={{ fontSize: 13 }}>Volunteer: {r.volunteerName}</p>}
            {r.status === 'ASSIGNED' && (
              <button className="primary" onClick={() => markCompleted(r.id)}>Mark Completed</button>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}
