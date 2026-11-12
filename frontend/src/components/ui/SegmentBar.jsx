// pixel meter til stats
export default function SegmentBar({ segments = 10, filled, tone = "sun", label, valueNow, valueMax }) {
    return (
        <div
            className="seg-bar"
            style={{ "--bar": `var(--${tone})` }}
            role="meter"
            aria-label={label}
            aria-valuemin={0}
            aria-valuemax={valueMax}
            aria-valuenow={valueNow}
        >
            {Array.from({ length: segments }, (_, i) => (
                <span key={i} className={i < filled ? "seg on" : "seg"} />
            ))}
        </div>
    )
}
