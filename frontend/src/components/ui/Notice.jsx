import { useEffect } from "react"

const VISIBLE_MS = 6_000

export default function Notice({ message, onDismiss }) {
    useEffect(() => {
        if (!message) return
        const id = setTimeout(onDismiss, VISIBLE_MS)
        return () => clearTimeout(id)
    }, [message, onDismiss])

    if (!message) return null
    return (
        <button type="button" className="notice" role="alert" onClick={onDismiss}>
            {message}
        </button>
    )
}
