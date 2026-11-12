import { speciesOf } from "@/data/species.js"

export default function PetSprite({ pet, mood, reaction }) {
    return (
        <img
            src={`/sprites/${pet.species.toLowerCase()}.svg`}
            alt={`${pet.name} the ${speciesOf(pet.species).label}`}
            className="pet-sprite"
            data-mood={mood?.key}
            data-reaction={reaction ?? undefined}
            draggable={false}
        />
    )
}