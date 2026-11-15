import { speciesOf } from "@/data/species.js"
import { PERSONALITY_LABELS, labelOf } from "@/data/petLabels.js"

export default function PetPanel({ pet }) {
    return (
        <div className="pet-nameplate">
            <strong>{pet.name}{pet.variant && " ✦"}</strong>
            <span>{speciesOf(pet.species).label} · {labelOf(PERSONALITY_LABELS, pet.personality)}</span>
        </div>
    )
}