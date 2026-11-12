import { useCallback, useEffect, useRef, useState } from "react"
import { getMonologue } from "@/api/petApi.js"

const COOLDOWN_MS = 4_000    // hvert kald rammer gemini. undgår spam
const BUBBLE_MS = 15_000

export function useMonologue(petId, { onError }) {
    const [text, setText] = useState("")
    const [thinking, setThinking] = useState(false)
    const lockedUntil = useRef(0)
    const hideTimer = useRef(null)

    const clear = useCallback(() => {
        clearTimeout(hideTimer.current)
        setText("")
    }, [])

    const speak = useCallback(async () => {
        if (!petId || thinking || Date.now() < lockedUntil.current) return
        lockedUntil.current = Date.now() + COOLDOWN_MS
        setThinking(true)
        try {
            const data = await getMonologue(petId)
            setText(data.dialogue)
            clearTimeout(hideTimer.current)
            hideTimer.current = setTimeout(() => setText(""), BUBBLE_MS)
        } catch (err) {
            onError(err.message)
        } finally {
            setThinking(false)
        }
    }, [petId, thinking, onError])

    useEffect(() => () => clearTimeout(hideTimer.current), [])

    return { text, thinking, speak, clear }
}
