export default function ConfirmDialog({ title = "CONFIRM.EXE", message, onConfirm, onCancel, busy }) {
    return (
        <div className="dialog-backdrop">
            <div className="retro-window dialog-window" role="dialog">
                <div className="window-header">
                    <div className="window-controls">
                        <span onClick={onCancel} title="Cancel"></span>
                        <span></span><span></span>
                    </div>
                    <span>{title}</span>
                </div>

                <div className="window-content dialog-content">
                    <p>{message}</p>

                    <div className="dialog-buttons">
                        <button className="dock-icon" onClick={onCancel} disabled={busy}>NO</button>
                        <button
                            className="dock-icon"
                            onClick={onConfirm}
                            disabled={busy}
                            style={{ background: "var(--accent-green)" }}
                        >
                            {busy ? "..." : "YES"}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}