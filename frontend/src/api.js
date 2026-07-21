import axios from 'axios'

// All requests go through the API Gateway (Spring Cloud Gateway), which
// routes each /api/... path to the right microservice via Eureka.
const GATEWAY_BASE_URL = 'http://localhost:8080'

const api = axios.create({
  baseURL: GATEWAY_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
})

export const UserAPI = {
  register: (data) => api.post('/api/users/register', data),
  login: (data) => api.post('/api/users/login', data),
}

export const RescueRequestAPI = {
  create: (data) => api.post('/api/rescue-requests', data),
  getAll: () => api.get('/api/rescue-requests'),
  getByStatus: (status) => api.get(`/api/rescue-requests/status/${status}`),
  getByCitizen: (citizenId) => api.get(`/api/rescue-requests/citizen/${citizenId}`),
  updateStatus: (id, status) => api.put(`/api/rescue-requests/${id}/status`, { status }),
}

export const VolunteerAPI = {
  create: (data) => api.post('/api/volunteers', data),
  getAll: () => api.get('/api/volunteers'),
  getAvailable: () => api.get('/api/volunteers/available'),
  assign: (volunteerId, requestId) => api.put(`/api/volunteers/${volunteerId}/assign/${requestId}`),
}

export const ShelterAPI = {
  create: (data) => api.post('/api/shelters', data),
  getAll: () => api.get('/api/shelters'),
  assignPeople: (shelterId, data) => api.put(`/api/shelters/${shelterId}/assign`, data),
}

export default api
