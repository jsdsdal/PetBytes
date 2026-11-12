export default function BootScreen({ message, onRetry }) {
    return (
        <div className="desktop desktop--boot">
            <div className="boot px-box">
                <p>{message}</p>
                {onRetry && <button type="button" className="px-btn" onClick={onRetry}>Try again</button>}
            </div>
        </div>
    )
}
