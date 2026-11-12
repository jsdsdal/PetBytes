import { useState, useEffect } from "react"
import { Button } from "@/components/ui/pixelact-ui/button"
import { getMyPets } from "@/api/petApi.js"
import { apiFetch } from "@/api/apiFetch.js"

export default function GamePage({ user, onLogout }) {
    const [pet, setPet] = useState(null)
    const [activity, setActivity] = useState([])
    const [monologue, setMonologue] = useState("")
    const [loading, setLoading] = useState(true)
    const [busy, setBusy] = useState(false)
    const [error, setError] = useState("")

    useEffect(() => {
        loadPet()
    }, [])

    async function loadPet() {
        setLoading(true)
        setError("")
        try {
            const pets = await getMyPets()
            // backend kan opbevare både aktive og inaktive pets -> vi tager den aktive
            const active = pets.find(p => p.status === "ACTIVE") ?? pets[0]
            if (!active) {
                setError("No pet found — go adopt one first.")
                return
            }
            setPet(active)
            await loadActivity(active.id)
        } catch (err) {
            setError(err.message)
        } finally {
            setLoading(false)
        }
    }

    async function loadActivity(petId) {
        try {
            const logs = await apiFetch(`/api/pets/${petId}/activity?limit=10`)
            setActivity(logs)
        } catch {
            // activity er dekorativt
        }
    }

    async function doAction(action) {
        if (busy) return
        setBusy(true)
        setError("")
        try {
            const updated = await apiFetch(`/api/pets/${pet.id}/${action}`, { method: "POST" })
            setPet(updated)
            await loadActivity(pet.id)
        } catch (err) {
            setError(err.message)
        } finally {
            setBusy(false)
        }
    }

    async function speak() {
        if (busy) return
        setBusy(true)
        setError("")
        try {
            const data = await apiFetch(`/api/pets/${pet.id}/monologue`)
            setMonologue(data.dialogue)
        } catch (err) {
            setError(err.message)
        } finally {
            setBusy(false)
        }
    }

    if (loading) return <p>Loading your pet...</p>
    if (!pet) return <p className="error">{error || "No pet found."}</p>

    return (
        <section className="pixel-box">
            <p className="muted">Welcome, {user}!</p>

            <h2>{pet.name}</h2>
            <p className="muted">
                {pet.species} · {pet.personality}
                {pet.variant && " ✨ (variant)"}
            </p>

            {/* Placeholder stats .
                OBS: hunger er inverted (0 = full, 100 = starving). */}
            <div className="stack">
                <div>Hunger: {pet.hunger} (lower is better)</div>
                <div>Happiness: {pet.happiness}</div>
                <div>Energy: {pet.energy}</div>
            </div>

            <div className="stack">
                <Button onClick={() => doAction("feed")} disabled={busy}>Feed</Button>
                <Button onClick={() => doAction("play")} disabled={busy}>Play</Button>
                <Button onClick={() => doAction("sleep")} disabled={busy}>Sleep</Button>
                <Button onClick={speak} disabled={busy}>Say something</Button>
            </div>

            {monologue && (
                <div className="pixel-box">
                    <p><strong>{pet.name}:</strong> {monologue}</p>
                </div>
            )}

            {error && <p className="error">{error}</p>}

            <h3>Recent activity</h3>
            {activity.length === 0 && <p className="muted">Nothing yet.</p>}
            <ul>
                {activity.map(a => (
                    <li key={a.id}>
                        {a.description}{" "}
                        <span className="muted">
                            — {new Date(a.occurredAt).toLocaleString()}
                        </span>
                    </li>
                ))}
            </ul>

            <Button variant="link" onClick={onLogout}>Log out</Button>
        </section>
    )
}