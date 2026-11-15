export const SPECIES = {
    CAT:{ label: "Cat",    color: "#b9a5ff", glyph: "C" },
    SLIME: { label: "Slime",    color: "#4fe3b5", glyph: "S" },
    TAMAGOTCHI: { label: "Tamagotchi",    color: "#ff9ec7", glyph: "T" },
    ELECTRIC_RAT: { label: "Electric Rat", color: "#ffe066", glyph: "E" },
}

export const speciesOf = (key) => SPECIES[key] ?? { label: key, color: "#cfc8e6", glyph: "?" }
