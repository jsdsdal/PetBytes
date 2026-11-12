import { logout } from "./userApi.js"

export const UNAUTHORIZED_EVENT = "petbytes:unauthorized"

export class ApiError extends Error {
    constructor(status, message) {
        super(message)
        this.status = status
    }
}

// ingen errors i backend, så vi fix faxer nogle her
const FRIENDLY = {
    401: "Your session ended. Please log in again.",
    404: "Couldn't find that.",
    409: "That isn't possible right now.",
    429: "Too many tries. Come back later.",
}

export async function apiFetch(path, options = {}) {
    const response = await send(path, options)

    if (response.status === 401) {
        logout()
        window.dispatchEvent(new Event(UNAUTHORIZED_EVENT))
    }
    if (!response.ok) {
        throw new ApiError(response.status, FRIENDLY[response.status] ?? `Something went wrong (${response.status}).`)
    }
    return response.status === 204 ? null : response.json()
}

async function send(path, options) {
    const token = sessionStorage.getItem("token")
    const headers = {}
    if (options.body) headers["Content-Type"] = "application/json"
    if (token) headers.Authorization = token

    try {
        return await fetch(path, { method: options.method ?? "GET", body: options.body, headers })
    } catch {
        throw new ApiError(0, "Can't reach the server.")
    }
}
