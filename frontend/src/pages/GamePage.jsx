import { useState, useEffect } from "react"
import { usePet } from "@/hooks/usePet.js"
import { useActivity } from "@/hooks/useActivity.js"
import { useInteractions } from "@/hooks/useInteractions.js"
import { useMonologue } from "@/hooks/useMonologue.js"
import StatBar from "@/components/pet/StatBar.jsx"
import CollectionWindow from "@/components/collection/CollectionWindow.jsx"
import ConfirmDialog from "@/components/ui/ConfirmDialog.jsx"
import { shelvePet, activatePet } from "@/api/petApi.js"
import PetPanel from "@/components/pet/PetPanel.jsx";

export default function GamePage({ user, onLogout, onNoPet }) {
    const { pet, status, error, version, apply, retry, switchTo } = usePet()
    const [notice, setNotice] = useState("")

    const activity = useActivity(pet?.id, version, 10)
    const interactions = useInteractions(pet?.id, { onUpdated: apply, onError: setNotice })
    const monologue = useMonologue(pet?.id, { onError: setNotice })
    const [showCollection, setShowCollection] = useState(false)
    const [showLog, setShowLog] = useState(true)
    const [confirm, setConfirm] = useState(null)
    const [working, setWorking] = useState(false)

//snakker hver 45 s
    useEffect(() => {
        if (status !== "ready") return
        const interval = setInterval(() => {
            if (!monologue.thinking && !interactions.busy) {
                monologue.speak()
            }
        }, 45000)
        return () => clearInterval(interval)
    }, [status, monologue.thinking, interactions.busy])

    // intet aktivt pet -> til adoption
    useEffect(() => {
        if (status === "empty") onNoPet()
    }, [status, onNoPet])

    function handleInteract(action) {
        monologue.clear()
        interactions.perform(action)
    }

    function askDaycare() {
        setConfirm({
            message: `Send ${pet.name} to daycare? Don't worry, this isn't permanent. You can bring ${pet.name} back any time from Collection → Daycare.`,
            onYes: sendToDaycare,
        })
    }

    async function sendToDaycare() {
        setWorking(true)
        try {
            await shelvePet(pet.id)
            onNoPet()
        } catch (err) {
            setNotice(err.message)
            setWorking(false)
            setConfirm(null)
        }
    }

    function askSwap(newPet) {
        setConfirm({
            message: `Swap ${pet.name} for ${newPet.name}? ${pet.name} goes to daycare. You can always swap back.`,
            onYes: () => swapTo(newPet),
        })
    }

    async function swapTo(newPet) {
        setWorking(true)
        try {
            await shelvePet(pet.id)                       // 1. det aktive pet i daycare
            const fresh = await activatePet(newPet.id)    // 2. det nye pet ud
            switchTo(fresh)                               // 3. fortæl usePet om det nye pet
            setShowCollection(false)                      // luk vinduet, listen er forældet nu
        } catch (err) {
            setNotice(err.message)
            retry()   // hent igen, så vi ved hvad der faktisk er aktivt
        } finally {
            setWorking(false)
            setConfirm(null)
        }
    }

    function askLogout() {
        setConfirm({
            message: `Log out? ${pet.name} keeps living while you're away, so don't stay away too long!`,
            onYes: onLogout,
        })
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
    const spriteUrl = pet.variant
        ? `/sprites/${spriteName}_variant.svg`
        : `/sprites/${spriteName}.svg`
    const isBusy = interactions.busy || monologue.thinking

    return (
        <div className="os-desktop">

            {/* desktop icons */}
            <div className="desktop-icons">
                <div className="desk-icon">
                    <div className="desk-icon-img">📄</div>
                    <span>README</span>
                </div>
                <div className="desk-icon" onClick={() => setShowCollection((open) => !open)}>
                    <div className="desk-icon-img">📁</div>
                    <span>COLLECTION</span>
                </div>
                <div className="desk-icon" onClick={() => setShowLog((open) => !open)}>
                    <div className="desk-icon-img">💬</div>
                    <span>LOG_CHAT</span>
                </div>
                <div className="desk-icon">
                    <div className="desk-icon-img">🗑️</div>
                    <span>TRASH</span>
                </div>
            </div>

            {/* activity log */}
            {showLog && (
                <aside className="retro-window log-window">
                    <div className="window-header">
                        <div className="window-controls">
                            <span onClick={() => setShowLog(false)} title="Close"></span>
                            <span></span><span></span>
                        </div>
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
            )}

            {/* Main Pet Window */}
            <main className="retro-window pet-window">
                <div className="window-header">
                    <div className="window-controls">
                        <span onClick={askDaycare} title="Send to daycare"></span>
                        <span></span><span></span>
                    </div>
                    <span>PET_VIEW.EXE</span>
                </div>
                <div className="window-content">

                    <PetPanel pet={pet} />

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

            {/* collection */}
            {showCollection && (
                <CollectionWindow
                    onClose={() => setShowCollection(false)}
                    onSwap={askSwap}
                    busy={working}
                />
            )}

            {/* floating dock */}
            <nav className="pixel-dock">
                <button onClick={() => handleInteract("feed")} disabled={isBusy} className="dock-icon" title="Feed">FEED</button>
                <button onClick={() => handleInteract("play")} disabled={isBusy} className="dock-icon" title="Play">PLAY</button>
                <button onClick={() => handleInteract("sleep")} disabled={isBusy} className="dock-icon" title="Sleep">SLEEP</button>

                <div className="dock-divider"></div>

                <button onClick={monologue.speak} disabled={isBusy} className="dock-icon" title="Talk" style={{ background: "var(--accent-green)" }}>TALK</button>

                <div className="dock-divider"></div>

                <button onClick={askLogout} className="dock-icon" title="Log out" style={{ background: "var(--accent-red)" }}>EXIT</button>
            </nav>

            {/* confirmation */}
            {confirm && (
                <ConfirmDialog
                    message={confirm.message}
                    onConfirm={confirm.onYes}
                    onCancel={() => setConfirm(null)}
                    busy={working}
                />
            )}

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