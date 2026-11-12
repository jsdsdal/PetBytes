// bestemmer hvordan petser ud

export function petMeters(pet) {
    return [
        { key: "food",   label: "Food",   value: 100 - pet.hunger, tone: "sun",       slot: "stat.food" },
        { key: "happy",  label: "Happy",  value: pet.happiness,    tone: "bubblegum", slot: "stat.happy" },
        { key: "energy", label: "Energy", value: pet.energy,       tone: "sky",       slot: "stat.energy" },
    ]
}

export function petMood(pet) {
    if (pet.hunger >= 75)    return { key: "hungry", label: "Hungry" }
    if (pet.energy <= 25)    return { key: "tired",  label: "Sleepy" }
    if (pet.happiness <= 25) return { key: "sad",    label: "Bored" }
    if (pet.happiness >= 70) return { key: "happy",  label: "Happy" }
    return { key: "okay", label: "Chilling" }
}
