import { useState, useEffect } from "react"
import { Button } from "@/components/ui/pixelact-ui/button"
import { Input } from "@/components/ui/pixelact-ui/input"
import { Label } from "@/components/ui/pixelact-ui/label"
import PetSprite from "@/components/pet/PetSprite.jsx"
import { getOffers, rerollOffers, acceptOffer } from "@/api/adoptionApi.js"
import DaycareList from "@/components/collection/DaycareList.jsx";
import { activatePet } from "@/api/petApi.js"

export default function AdoptionPage({ onAdopted }) {
    const [offers, setOffers] = useState([])
    const [rerollsLeft, setRerollsLeft] = useState(0)
    const [selectedId, setSelectedId] = useState(null)
    const [name, setName] = useState("")
    const [loading, setLoading] = useState(true)
    const [submitting, setSubmitting] = useState(false)
    const [error, setError] = useState("")

    useEffect(() => { load() }, [])

    async function load() {
        setLoading(true); setError("")
        try {
            const data = await getOffers()
            applyBatch(data)
        } catch (err) {
            setError(err.message)
        } finally {
            setLoading(false)
        }
    }

    function applyBatch(data) {
        setOffers(data.offers)
        setRerollsLeft(data.rerollsLeft)
        setSelectedId(null)
        setName("")
    }

    async function handleReroll() {
        if (submitting || rerollsLeft <= 0) return
        setSubmitting(true); setError("")
        try {
            applyBatch(await rerollOffers())
        } catch (err) {
            setError(err.message)
        } finally {
            setSubmitting(false)
        }
    }

    async function handleAccept(e) {
        e.preventDefault()
        if (submitting || !selectedId) return
        setSubmitting(true)
        try {
            const pet = await acceptOffer(selectedId, name.trim())
            onAdopted(pet)
        } catch (err) {
            setError(err.message)
            setSubmitting(false)
        }
    }

    async function handleBringBack(pet) {
        if (submitting) return
        setSubmitting(true)
        try {
            await activatePet(pet.id)
            onAdopted()
        } catch (err) {
            setError(err.message)
            setSubmitting(false)
        }
    }

    if (loading) return <p>Looking for pets...</p>
    if (error && offers.length === 0) return <p className="error">{error}</p>

    return (
        <>
        <form className="pixel-box adoption" onSubmit={handleAccept}>
            <h2>A few pets are waiting</h2>
            <p className="muted">
                Pick one to adopt. Rerolls left today: <strong>{rerollsLeft}</strong>
            </p>

            <div className="offer-grid">
                {offers.map((offer) => {
                    const isSelected = offer.id === selectedId
                    return (
                        <button
                            type="button"
                            key={offer.id}
                            className={`offer-card${isSelected ? " is-selected" : ""}`}
                            onClick={() => { setSelectedId(offer.id); setName("") }}
                            disabled={submitting}
                            aria-pressed={isSelected}
                        >
                            <PetSprite
                                species={offer.species}
                                variant={offer.variant}
                            />
                            <span className="offer-card__species">{offer.species}</span>
                            <span className="offer-card__personality">{offer.personality}</span>
                            {offer.variant && <span className="offer-card__variant">✨ variant</span>}
                        </button>
                    )
                })}
            </div>

            <Button
                type="button"
                variant="secondary"
                onClick={handleReroll}
                disabled={submitting || rerollsLeft <= 0}
                className="w-full whitespace-normal h-auto py-2"
                style={{ width: '100%', whiteSpace: 'normal', height: 'auto' }}
            >
                {rerollsLeft > 0
                    ? `Show me ${offers.length} different ones (${rerollsLeft} reroll${rerollsLeft === 1 ? "" : "s"} left)`
                    : "No rerolls left today"}
            </Button>

            {selectedId && (
                <>
                    <Label className="field">
                        Name your new pet
                        <Input
                            required
                            maxLength={20}
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                    </Label>
                    <Button type="submit" disabled={submitting || !name.trim()}>
                        Adopt
                    </Button>
                </>
            )}

            {error && <p className="error">{error}</p>}
        </form>

            <section className="pixel-box">
                <h2>Or bring a pet back from daycare</h2>
                <DaycareList buttonLabel="BRING BACK" onPick={handleBringBack} busy={submitting} />
            </section>
        </>
    )
}