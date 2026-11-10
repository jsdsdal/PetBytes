export const SPECIES = [
    { key: 'CAT',    designKey: 'cat',    description: 'A small, whiskered creature.' },
    { key: 'FOX',    designKey: 'fox',    description: 'A sharp-eared, bushy-tailed creature.' },
    { key: 'OWL',    designKey: 'owl',    description: 'A round-eyed, feathered creature.' },
    { key: 'TURTLE', designKey: 'turtle', description: 'A slow, shelled creature.' },
]

export const PET_PERSONALITIES = ['CONTEMPLATIVE', 'SPOILED', 'ENERGETIC', 'DOCILE']
export const LIFE_STAGES = ['KID', 'YOUNG', 'MATURE', 'DEAD', 'RAN_AWAY']

// matches PetResponse: id, name, species, personality, lifeStage, hunger, happiness, energy
export const MOCK_PETS = [
    { id: 1, name: 'Mochi', species: 'CAT',    personality: 'CONTEMPLATIVE', lifeStage: 'KID',    hunger: 50, happiness: 50, energy: 50 },
    { id: 2, name: 'Biscuit', species: 'FOX',  personality: 'DOCILE',        lifeStage: 'YOUNG',  hunger: 92, happiness: 20, energy: 15 }, // neglected
    { id: 3, name: 'Zap', species: 'TURTLE',   personality: 'ENERGETIC',     lifeStage: 'MATURE', hunger: 10, happiness: 95, energy: 90 }, // thriving
    { id: 4, name: 'Duchess', species: 'OWL',  personality: 'SPOILED',       lifeStage: 'DEAD',   hunger: 100, happiness: 0, energy: 0 },  // edge case
]

// proposed shape for the not-yet-built GET /api/pets/{id}/activity
export const MOCK_ACTIVITY = [
    { id: 3, type: 'PETTED', amount: null, occurredAt: '2026-11-10T09:30:00Z', description: 'The player petted the pet.' },
    { id: 2, type: 'FED',    amount: 30,   occurredAt: '2026-11-10T08:10:00Z', description: 'The pet ate food from its bowl.' },
    { id: 1, type: 'NAPPED', amount: 20,   occurredAt: '2026-11-10T06:45:00Z', description: 'The pet took a nap.' },
]