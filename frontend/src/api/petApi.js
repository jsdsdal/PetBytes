import { apiFetch } from "./apiFetch.js"

export function getMyPets() {
    return apiFetch("/api/pets")
}
