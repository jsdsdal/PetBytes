import { apiFetch } from "./apiFetch.js"

export const getOffers = () => apiFetch("/api/adoption/offers")

export const rerollOffers = () =>
    apiFetch("/api/adoption/offers/reroll", { method: "POST" })

export const acceptOffer = (offerId, name) =>
    apiFetch(`/api/adoption/offers/${offerId}/accept`, {
        method: "POST",
        body: JSON.stringify({ name }),
    })