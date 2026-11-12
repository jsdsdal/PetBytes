import { useCallback, useEffect, useRef, useState } from "react"
import { getMyPets, getPet } from "@/api/petApi.js"

const POLL_MS = 15_000 // poll hver 15 sek i stedet for 60 til demo
const findActive = (pets) => pets.find((p) => p.status === "ACTIVE") ?? null

// loader aktivt pet og kun aktiv
export function usePet() {
    const [pet, setPet] = useState(null)
    const [status, setStatus] = useState("loading")   // loading | ready | empty | error
    const [error, setError] = useState("")
    const [version, setVersion] = useState(0)         // bump på "ny" pet
    const petId = useRef(null)
    const hasLoaded = useRef(false)

    const apply = useCallback((next) => {
        setPet(next)
        setVersion((v) => v + 1)
    }, [])

    const load = useCallback(async () => {
        try {
            const fresh = petId.current ? await getPet(petId.current) : findActive(await getMyPets())
            if (!fresh || fresh.status !== "ACTIVE") {
                petId.current = null
                setPet(null)
                setStatus("empty")
                return
            }
            petId.current = fresh.id
            hasLoaded.current = true
            apply(fresh)
            setStatus("ready")
        } catch (err) {
            // fanger pet i baggrunden -> går ikke tabt (måske? forhåbentligt?)
            if (!hasLoaded.current) {
                setError(err.message)
                setStatus("error")
            }
        }
    }, [apply])

    useEffect(() => {
        load()
        const id = setInterval(load, POLL_MS)
        const onVisible = () => { if (!document.hidden) load() }
        document.addEventListener("visibilitychange", onVisible)
        return () => {
            clearInterval(id)
            document.removeEventListener("visibilitychange", onVisible)
        }
    }, [load])

    const retry = useCallback(() => {
        setStatus("loading")
        load()
    }, [load])

    return { pet, status, error, version, apply, retry }
}
