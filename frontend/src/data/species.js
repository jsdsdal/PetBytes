export const SPECIES = {
    CAT:    { label: "Cat",    color: "#ff9ec7", glyph: "C" },
    FOX:    { label: "Fox",    color: "#ffb36b", glyph: "F" },
    OWL:    { label: "Owl",    color: "#b9a5ff", glyph: "O" },
    TURTLE: { label: "Turtle", color: "#4fe3b5", glyph: "T" },
}

export const speciesOf = (key) => SPECIES[key] ?? { label: key, color: "#cfc8e6", glyph: "?" }
