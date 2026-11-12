import { useState, useEffect } from "react"
import { usePet } from "@/hooks/usePet.js"
import { useActivity } from "@/hooks/useActivity.js"
import { useInteractions } from "@/hooks/useInteractions.js"
import { useMonologue } from "@/hooks/useMonologue.js"
import StatBar from "@/components/pet/StatBar.jsx"

export default function GamePage({ user, onLogout, onNoPet }) {
    const { pet, status, error, version, apply, retry } = usePet()
    const [notice, setNotice] = useState("")

    const activity = useActivity(pet?.id, version, 10)
    const interactions = useInteractions(pet?.id, { onUpdated: apply, onError: setNotice })
    const monologue = useMonologue(pet?.id, { onError: setNotice })

//snakker hver 45 s
    useEffect(() => {
        if (status !== "ready") return

        const interval = setInterval(() => {
            // snakker kun hvis den ikke laver andet
            if (!monologue.thinking && !interactions.busy) {
                monologue.speak()
            }
        }, 45000) // 45 seconds

        return () => clearInterval(interval)
    }, [status, monologue, interactions.busy])

    function handleInteract(action) {
        monologue.clear()
        interactions.perform(action)
    }

    if (status === "loading" || status === "empty") {
        return <div style={{ padding: 20, fontFamily: "'Press Start 2P'" }}>Booting PetBytes OS...</div>
    }

    if (status === "error") {
        return (
            <div style={{ padding: 20, fontFamily: "'Press Start 2P'" }}>
                <p>{error}</p>
                <button className="dock-icon" onClick={retry}>RETRY</button>
            </div>
        )
    }

    const spriteName = pet.species.toLowerCase()
    const spriteUrl = `/sprites/${spriteName}.svg`
    const isBusy = interactions.busy || monologue.thinking

    return (
        <div className="os-desktop">

            {/* desktop icons */}
            <div className="desktop-icons">
                <div className="desk-icon">
                    <div className="desk-icon-img">📄</div>
                    <span>README</span>
                </div>
                <div className="desk-icon">
                    <div className="desk-icon-img">📁</div>
                    <span>ARCHIVE</span>
                </div>
                <div className="desk-icon">
                    <div className="desk-icon-img">🗑️</div>
                    <span>TRASH</span>
                </div>
            </div>

            {/* activity log */}
            <aside className="retro-window log-window">
                <div className="window-header">
                    <div className="window-controls"><span></span><span></span><span></span></div>
                    <span>LOG_CHAT.EXE</span>
                </div>
                <div className="window-content" style={{ padding: 0 }}>
                    <ul className="activity-list">
                        {activity.length === 0 && <li>No data found...</li>}
                        {activity.map(a => (
                            <li key={a.id} className={a.playerInitiated ? "player-log" : "pet-log"}>
                                <span>{a.playerInitiated ? "> YOU:" : "* PET:"}</span>
                                <span>{a.description}</span>
                            </li>
                        ))}
                    </ul>
                </div>
            </aside>

            {/* Main Pet Window */}
            <main className="retro-window pet-window">
                <div className="window-header">
                    <div className="window-controls">
                        <span onClick={onLogout} title="Log Out"></span>
                        <span></span><span></span>
                    </div>
                    <span>PET_VIEW.EXE</span>
                </div>
                <div className="window-content">

                    <div className="stats-row">
                        <StatBar label="FOOD" value={100 - pet.hunger} />
                        <StatBar label="HAPPY" value={pet.happiness} />
                        <StatBar label="ENERGY" value={pet.energy} />
                    </div>

                    <div className="sprite-container">
                        <img
                            src={spriteUrl}
                            alt={pet.name}
                            className="pet-sprite"
                            data-reaction={interactions.reaction}
                        />

                        {(monologue.text || monologue.thinking) && (
                            <div className="speech-bubble">
                                {monologue.thinking ? "Processing..." : monologue.text}
                            </div>
                        )}
                    </div>
                </div>
            </main>

            {/* floating dock */}
            <nav className="pixel-dock">
                <button onClick={() => handleInteract("feed")} disabled={isBusy} className="dock-icon" title="Feed">FEED</button>
                <button onClick={() => handleInteract("play")} disabled={isBusy} className="dock-icon" title="Play">PLAY</button>
                <button onClick={() => handleInteract("sleep")} disabled={isBusy} className="dock-icon" title="Sleep">SLEEP</button>

                <div className="dock-divider"></div>

                <button onClick={monologue.speak} disabled={isBusy} className="dock-icon" title="Talk" style={{ background: "var(--accent-green)" }}>TALK</button>

                <div className="dock-divider"></div>

                <button onClick={onLogout} className="dock-icon" title="Log out" style={{ background: "var(--accent-red)" }}>EXIT</button>
            </nav>

            {/* notice */}
            {notice && (
                <div className="retro-window" style={{ bottom: "100px", left: "50%", transform: "translateX(-50%)", zIndex: 999, width: "300px" }} onClick={() => setNotice("")}>
                    <div className="window-header" style={{ background: "var(--accent-yellow)", fontSize: "10px" }}>
                        <span>SYSTEM ALERT</span>
                    </div>
                    <div className="window-content" style={{ padding: "16px", fontWeight: "bold", fontSize: "20px", textAlign: "center" }}>
                        {notice}
                    </div>
                </div>
            )}
        </div>
    )
}