export default function SpeechBubble({ text, thinking }) {
    if (!thinking && !text) return null
    return (
        <p className="bubble px-box" role="status">
            <span className="bubble__text">
                {thinking ? <span className="dots" aria-label="Thinking">...</span> : text}
            </span>
        </p>
    )
}
