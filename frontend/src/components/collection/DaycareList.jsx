import { useEffect, useState} from "react";
import {getMyPets} from "@/api/petApi.js";
import { speciesOf } from "@/data/species.js"
import { PERSONALITY_LABELS, labelOf } from "@/data/petLabels.js"

export default function DaycareList({buttonLabel, onPick, busy}) {
    const [pets, setPets] = useState(null)
    const [error, setError ] = useState("")

    useEffect(() => {
        getMyPets()
            .then((all) => setPets(all.filter((p) => p.status === "INACTIVE")))
            .catch((err) => setError(err.message))
    },[])

    if (error) return <p>{error}</p>
    if (!pets) return <p>Loading...</p>
    if (pets.length === 0) return <p>Daycare is empty.</p>

    return (
        <ul className="daycare-list">
            {pets.map((pet) => (
                <li key={pet.id} className="daycare-row">
                    <img
                        src={`/sprites/${pet.species.toLowerCase()}.svg`}
                        alt={speciesOf(pet.species).label}
                        draggable={false}
                    />

                    <div className="daycare-info">
                        <strong>{pet.name}{pet.variant && " ✦"}</strong>
                        <span>
                            {speciesOf(pet.species).label} · {labelOf(PERSONALITY_LABELS, pet.personality)}
                        </span>
                    </div>

                    <button className="dock-icon" onClick={() => onPick(pet)} disabled={busy}>
                        {buttonLabel}
                    </button>
                </li>
            ))}
        </ul>
    )
}