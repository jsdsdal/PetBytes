import { useEffect, useState } from "react"
import { getMyPets } from "@/api/petApi.js"
import { buildCollection, dexNumber } from "@/lib/collection.js"
import { speciesOf } from "@/data/species.js"
import { PERSONALITY_LABELS, labelOf } from "@/data/petLabels.js"
import DaycareList from "@/components/collection/DaycareList.jsx";

export default function CollectionWindow({ onClose,onSwap, busy }) {
    const [forms, setForms] = useState(null)
    const [error, setError] = useState("")
    const [tab, setTab] = useState("collection")


    useEffect(() => {
        getMyPets()
            .then((pets) => setForms(buildCollection(pets)))
            .catch((err) => setError(err.message))
    }, [])

    return (
        <section className="retro-window collection-window">
            <div className="window-header">
                <div className="window-controls">
                    <span onClick={onClose} title="Close"></span>
                    <span></span><span></span>
                </div>
                <span>{tab === "collection" ? "COLLECTION.EXE" : "DAYCARE.EXE"}</span>
            </div>

            <div className="window-tabs">
                <button
                    className={tab === "collection" ? "tab is-active" : "tab"}
                    onClick={() => setTab("collection")}
                >
                    COLLECTION
                </button>
                <button
                    className={tab === "daycare" ? "tab is-active" : "tab"}
                    onClick={() => setTab("daycare")}
                >
                    DAYCARE
                </button>
            </div>

            <div className="window-content">
                {tab === "collection"
                    ? <CollectionContent forms={forms} error={error} />
                    : <DaycareList buttonLabel="SWAP" onPick={onSwap} busy={busy} />}
            </div>
        </section>
    )
}

function CollectionContent({ forms, error }) {
    if (error) return <p>{error}</p>
    if (!forms) return <p>Loading...</p>
    if (forms.length === 0) return <p>No pets yet. Go adopt one!</p>

    return (
        <>
            <p className="collection-count">{forms.length} forms found</p>
            <ul className="collection-grid">
                {forms.map((form) => (
                    <CollectionCard key={form.key} form={form} />
                ))}
            </ul>
        </>
    )
}

function CollectionCard({ form }) {
    const species = speciesOf(form.species)

    return (
        <li className={`collection-card ${form.variant ? "is-variant" : ""}`}>
            <span className="dex-number">
                {dexNumber(form.species)} {form.variant && "✦"}
            </span>

            <img
                src={`/sprites/${form.species.toLowerCase()}${form.variant ? "_variant" : ""}.svg`}
                alt={species.label}
                draggable={false}
            />

            <strong>{species.label}</strong>
            <span>{labelOf(PERSONALITY_LABELS, form.personality)}</span>
            <small>Adopted ×{form.timesAdopted}</small>
            <small className="collection-names">{form.names.join(", ")}</small>
        </li>
    )
}