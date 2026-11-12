export const BOND_ORDER = ["STRANGER", "ACQUAINTANCE", "COMPANION", "FRIEND", "BONDED"]

export const BOND_LABELS = {
    STRANGER: "Stranger", ACQUAINTANCE: "Acquaintance", COMPANION: "Companion", FRIEND: "Friend", BONDED: "Bonded",
}
export const PERSONALITY_LABELS = {
    CONTEMPLATIVE: "Contemplative", SPOILED: "Spoiled", ENERGETIC: "Energetic", DOCILE: "Docile",
}
export const LIFE_STAGE_LABELS = { KID: "Kid", YOUNG: "Young", MATURE: "Mature" }

export const labelOf = (map, key) => map[key] ?? key
