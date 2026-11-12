export async function apiFetch(path, option = {}) {
    const token = sessionStorage.getItem("token")
    const headers = { "content-Type" : "application/json" }

    if (token) {
        headers.Authorization = token
    }

    const requestOption = {
        method: option.method,
        body: option.body,
        headers: headers,
    }

    const response = await fetch(path, requestOption)

    if (!response.ok) {
        throw new Error('Request failed (' + response.status + ')')
    }

    const data = await response.json()
    return data
}