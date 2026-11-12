const UNITS = [["d", 86_400], ["h", 3_600], ["m", 60]]

// format timestamps
export function timeAgo(iso, now = Date.now()) {
    const seconds = Math.max(0, Math.round((now - new Date(iso).getTime()) / 1000))
    const unit = UNITS.find(([, size]) => seconds >= size)
    if (!unit) return "just now"
    const [suffix, size] = unit
    return `${Math.floor(seconds / size)}${suffix} ago`
}
