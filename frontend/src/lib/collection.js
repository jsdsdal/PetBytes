import {SPECIES} from "@/data/species.js";
import {PERSONALITY_LABELS} from "@/data/petLabels.js";

const SPECIES_ORDER = Object.keys(SPECIES)
const PERSONALITY_ORDER = Object.keys(PERSONALITY_LABELS)

//unikke pets
export const formKey = (pet) => `${pet.species}-${pet.personality}-${pet.variant}`

export function dexNumber(species) {
    const index = SPECIES_ORDER.indexOf(species)
    return index === -1 ? "#???" : "#" + String(index + 1).padStart(3, "0")
}

export function buildCollection(pets) {
    const forms = new Map()

    for (const pet of pets) {
        const key = formKey(pet)
        const form = forms.get(key)

        if (form) {
            form.timesAdopted += 1
            form.names.push(pet.name)
        } else {
            forms.set(key, {
                key,
                species: pet.species,
                personality: pet.personality,
                variant: pet.variant,
                timesAdopted: 1,
                names: [pet.name],
            })
        }
    }
    return [...forms.values()].sort(compareForms)
}

function compareForms(a,b) {
    return (
        SPECIES_ORDER.indexOf(a.species) - SPECIES_ORDER.indexOf(b.species) ||
        PERSONALITY_ORDER.indexOf(a.personality) - PERSONALITY_ORDER.indexOf(b.personality) ||
        Number(a.variant) - Number(b.variant)
    )
}
