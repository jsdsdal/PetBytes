export async function login(username, password) {
    const response = await fetch("/api/dologin", {
        method: "POST",
        headers: { "Content-Type": "application/json",
            Authorization: "Basic " + btoa(username + ":" + password)},
        body: JSON.stringify({ username, password}),
    });
    if (!response.ok) {
        throw new Error("Wrong username or password")
    }

    const token = response.headers.get("Authorization")

    sessionStorage.setItem("token", token)
    sessionStorage.setItem("username", username)
}

export function logout() {
    sessionStorage.removeItem("token")
    sessionStorage.removeItem("username")
}

export async function register(username, email, password, personality) {
    const response = await fetch("/api/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password, personality}),
    });
    if (!response.ok) {
       const message = await response.text()
        throw new Error(message || "Could not create user")
    }
    return response.text()
}