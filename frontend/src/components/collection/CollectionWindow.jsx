import { useEffect, useState } from "react"
import { getMyPets } from "@/api/petApi.js"
import { buildCollection, dexNumber } from "@/lib/collection.js"
import { speciesOf } from "@/data/species.js"
import { PERSONALITY_LABELS, labelOf } from "@/data/petLabels.js"

export default function CollectionWindow({ onClose }) {
    const [forms, setForms] = useState(null)
    const [error, setError] = useState("")


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
                <span>COLLECTION.EXE</span>
            </div>

            <div className="window-content">
                <CollectionContent forms={forms} error={error} />
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
                src={`/sprites/${form.species.toLowerCase()}.svg`}
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