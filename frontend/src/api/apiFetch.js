export async function apiFetch(path, option = {}) {
    const token = sessionStorage.getItem("token")
    const headers = { ...(option.body ? { "Content-Type": "application/json" } : {}) }
    if (token) headers.Authorization = token

    const response = await fetch(path, {
        method: option.method ?? "GET",
        body: option.body,
        headers,
    })

    if (!response.ok) {
        throw new Error(`Request failed (${response.status})`)
    }
    return response.json()
}