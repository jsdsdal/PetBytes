import { MOCK_PETS, MOCK_ACTIVITY } from './mockData'

const pets = structuredClone(MOCK_PETS)
const logs = { 1: structuredClone(MOCK_ACTIVITY) }
const wait = (ms = 250) => new Promise((r) => setTimeout(r, ms))
const clamp = (v) => Math.min(100, Math.max(0, v))
const find = (id) => pets.find((p) => p.id === Number(id))

function apply(pet, { hunger = 0, happiness = 0, energy = 0 }) {
    pet.hunger = clamp(pet.hunger + hunger)
    pet.happiness = clamp(pet.happiness + happiness)
    pet.energy = clamp(pet.energy + energy)
}

function record(petId, type, amount) {
    logs[petId] ??= []
    logs[petId].unshift({ id: Date.now(), type, amount, occurredAt: new Date().toISOString(), description: type })
}

export async function getPet(id) {
    await wait()
    const pet = find(id)
    if (!pet) throw new Error('Pet not found')
    return { ...pet }
}

export async function feed(id, amount = 10) {
    await wait()
    const pet = find(id)
    apply(pet, { hunger: -amount, happiness: 2 })
    record(pet.id, 'FED', amount)
    return { ...pet }
}

export async function play(id, amount = 10) {
    await wait()
    const pet = find(id)
    apply(pet, { happiness: amount, energy: -Math.trunc(amount / 2), hunger: Math.trunc(amount / 4) })
    record(pet.id, 'PLAYED', amount)
    return { ...pet }
}

export async function sleep(id, amount = 10) {
    await wait()
    const pet = find(id)
    apply(pet, { energy: amount, hunger: Math.trunc(amount / 3) })
    record(pet.id, 'NAPPED', amount)
    return { ...pet }
}

export async function getActivity(id) {
    await wait()
    return [...(logs[id] ?? [])]
}