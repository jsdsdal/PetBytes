import { speciesOf } from "@/data/species.js"

export default function PetSprite({ pet, species, variant, name, mood, reaction }) {
    const s = species ?? pet?.species
    const v = variant ?? pet?.variant ?? false
    const n = name ?? pet?.name
    const label = speciesOf(s)?.label ?? s
    const file = v ? `${s.toLowerCase()}_variant.svg` : `${s.toLowerCase()}.svg`

    return (
        <img
            src={`/sprites/${file}`}
            alt={n ? `${n} the ${label}` : `A ${label}`}
            className="pet-sprite"
            data-mood={mood?.key}
            data-reaction={reaction ?? undefined}
            draggable={false}
        />
    )
}