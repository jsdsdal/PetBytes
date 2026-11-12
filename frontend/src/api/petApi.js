import { apiFetch } from "./apiFetch.js"

export const getMyPets = () => apiFetch("/api/pets")
export const getPet = (id) => apiFetch(`/api/pets/${id}`)
export const getActivity = (id, limit = 8) => apiFetch(`/api/pets/${id}/activity?limit=${limit}`)
export const getMonologue = (id) => apiFetch(`/api/pets/${id}/monologue`)

// action: "feed" | "play" | "sleep"
export const interact = (id, action) => apiFetch(`/api/pets/${id}/${action}`, { method: "POST" })
