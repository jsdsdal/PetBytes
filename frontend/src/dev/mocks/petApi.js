import * as mock from './mockApi'

const USE_MOCK = import.meta.env.VITE_USE_MOCK === 'true'

async function request(path, options) {
    const res = await fetch(`/api/pets${path}`, options)
    if (!res.ok) throw new Error(`HTTP ${res.status}`)
    return res.json()
}

const post = (path, amount) =>
    request(path, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ amount }),
    })

export const getPet      = USE_MOCK ? mock.getPet      : (id) => request(`/${id}`)
export const feed        = USE_MOCK ? mock.feed        : (id, amount) => post(`/${id}/feed`, amount)
export const play        = USE_MOCK ? mock.play        : (id, amount) => post(`/${id}/play`, amount)
export const sleep       = USE_MOCK ? mock.sleep       : (id, amount) => post(`/${id}/sleep`, amount)
export const getActivity = USE_MOCK ? mock.getActivity : (id) => request(`/${id}/activity`)