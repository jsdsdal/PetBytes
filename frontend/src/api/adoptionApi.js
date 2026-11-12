import { apiFetch } from "./apiFetch.js"

export function getCurrentOffer() {
    return apiFetch("/api/adoption/offers/current")
}

export function declineOffer(offerId) {
    return apiFetch(`/api/adoption/offers/${offerId}/decline`, { method: "POST" })
}

export function acceptOffer(offerId, name) {
    return apiFetch(`/api/adoption/offers/${offerId}/accept`, {
        method: "POST",
        body: JSON.stringify({ name }),
    })
}