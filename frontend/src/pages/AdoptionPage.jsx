// src/pages/AdoptionPage.jsx
import { useState, useEffect } from "react"
import { Button } from "@/components/ui/pixelact-ui/button"
import { Input } from "@/components/ui/pixelact-ui/input"
import { Label } from "@/components/ui/pixelact-ui/label"
import { getCurrentOffer, declineOffer, acceptOffer } from "@/api/adoptionApi.js"

export default function AdoptionPage({ onAdopted }) {
    const [offer, setOffer] = useState(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState("")
    const [name, setName] = useState("")
    const [submitting, setSubmitting] = useState(false)

//kører 1x når component hentes
    useEffect(() => {
        loadOffer()
    }, [])

    async function loadOffer() {
        setLoading(true)
        setError("")
        try {
            const data = await getCurrentOffer()
            setOffer(data)
        } catch (err) {
            setError(err.message)
        } finally {
            setLoading(false)
        }
    }

    async function handleDecline() {
        if (submitting) return
        setSubmitting(true)
        try {
            const next = await declineOffer(offer.id)
            setOffer(next)
            setName("")
        } catch (err) {
            setError(err.message)
        } finally {
            setSubmitting(false)
        }
    }

    async function handleAccept(e) {
        e.preventDefault()
        if (submitting) return
        setSubmitting(true)
        try {
            const pet = await acceptOffer(offer.id, name.trim())
            onAdopted(pet)   // skifter skærm
        } catch (err) {
            setError(err.message)
            setSubmitting(false)
        }
    }

    if (loading) return <p>Looking for a pet...</p>
    if (!offer) return <p className="error">{error || "No offer available."}</p>
    if (!offer) return <p>No offer available.</p>

    return (
        <form className="pixel-box" onSubmit={handleAccept}>
            <h2>A pet is waiting for you</h2>

            <p>
                Species: <strong>{offer.species}</strong><br />
                Personality: <strong>{offer.personality}</strong>
                {offer.variant && " ✨ (variant)"}
            </p>

            <Label className="field">
                Give it a name
                <Input
                    required
                    maxLength={20}
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
            </Label>

            <p className="muted">Declines left today: {offer.declinesLeft}</p>

            {error && <p className="error">{error}</p>}

            <Button type="submit" disabled={submitting || !name.trim()}>
                Adopt
            </Button>
            <Button type="button" variant="link" onClick={handleDecline} disabled={submitting || offer.declinesLeft <= 0}>
                Not this one — show me another
            </Button>
        </form>
    )
}