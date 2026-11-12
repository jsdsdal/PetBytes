import { useCallback, useEffect, useRef, useState } from "react"
import { interact } from "@/api/petApi.js"

const REACTION_MS = 1400

// feed / play / sleep. `reaction`.
// animerer spriten
export function useInteractions(petId, { onUpdated, onError }) {
    const [busy, setBusy] = useState(false)
    const [reaction, setReaction] = useState(null)
    const busyRef = useRef(false)
    const timer = useRef(null)

    const showReaction = useCallback((action) => {
        clearTimeout(timer.current)
        setReaction(action)
        timer.current = setTimeout(() => setReaction(null), REACTION_MS)
    }, [])

    const perform = useCallback(async (action) => {
        if (!petId || busyRef.current) return
        busyRef.current = true
        setBusy(true)
        try {
            onUpdated(await interact(petId, action))
            showReaction(action)
        } catch (err) {
            onError(err.message)
        } finally {
            busyRef.current = false
            setBusy(false)
        }
    }, [petId, onUpdated, onError, showReaction])

    useEffect(() => () => clearTimeout(timer.current), [])

    return { busy, reaction, perform }
}
