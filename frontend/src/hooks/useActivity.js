import { useEffect, useState } from "react"
import { getActivity } from "@/api/petApi.js"

export function useActivity(petId, version, limit = 8) {
    const [items, setItems] = useState([])

    useEffect(() => {
        if (!petId) return
        let stale = false
        getActivity(petId, limit)
            .then((data) => { if (!stale) setItems(data) })
            .catch(() => { })
        return () => { stale = true }
    }, [petId, version, limit])

    return items
}
