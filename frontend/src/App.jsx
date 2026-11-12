import './App.css'
import { useCallback, useEffect, useState } from 'react'
import QuizPage from "@/pages/QuizPage.jsx"
import LoginPage from "@/pages/LoginPage.jsx"
import RegisterPage from "@/pages/RegisterPage.jsx"
import AdoptionPage from "@/pages/AdoptionPage.jsx"
import GamePage from "@/pages/GamePage.jsx"
import { Button } from "@/components/ui/pixelact-ui/button"
import { login, logout, register } from "@/api/userApi.js"
import { getMyPets } from "@/api/petApi.js"
import { UNAUTHORIZED_EVENT } from "@/api/apiFetch.js"

// Game needs a pet that is ACTIVE. Having only shelved pets means: go adopt.
async function landingScreen() {
    try {
        const pets = await getMyPets()
        return pets.some((p) => p.status === 'ACTIVE') ? 'game' : 'adoption'
    } catch (err) {
        return err.status === 401 ? 'login' : 'offline'   // a 500 shouldn't log you out
    }
}

function App() {
    const [screen, setScreen] = useState('loading')
    const [user, setUser] = useState(sessionStorage.getItem('username'))
    const [draft, setDraft] = useState(null)               // register form values
    const [personality, setPersonality] = useState(null)   // quiz result, kept if signup fails
    const [registerError, setRegisterError] = useState("")

    useEffect(() => {
        const onExpired = () => { setUser(null); setScreen('login') }
        window.addEventListener(UNAUTHORIZED_EVENT, onExpired)
        return () => window.removeEventListener(UNAUTHORIZED_EVENT, onExpired)
    }, [])

    useEffect(() => {
        if (!sessionStorage.getItem('token')) {
            setScreen('login')
            return
        }
        landingScreen().then(setScreen)
    }, [])

    async function handleLogin(username) {
        setUser(username)
        setScreen(await landingScreen())
    }

    function handleLogout() {
        logout()
        setUser(null)
        setScreen('login')
    }

    function handleRegisterContinue(formData) {
        setDraft(formData)
        setRegisterError('')
        if (personality) createAccount(formData, personality)   // already took the quiz
        else setScreen('quiz')
    }

    function handleQuizFinished(result) {
        setPersonality(result)
        createAccount(draft, result)
    }

    async function createAccount(account, chosenPersonality) {
        try {
            await register(account.username, account.email, account.password, chosenPersonality)
        } catch (err) {
            setRegisterError(err.message)
            setScreen('register')
            return
        }
        try {
            await login(account.username, account.password)
            setUser(account.username)
            setScreen('adoption')
        } catch {
            setScreen('login')   // account exists now; don't try to register it twice
        }
    }

    const goToAdoption = useCallback(() => setScreen('adoption'), [])

    if (screen === 'game') {
        return <GamePage user={user} onLogout={handleLogout} onNoPet={goToAdoption} />
    }

    return (
        <main className="page">
            <h1>PetBytes</h1>

            {screen === 'loading' && <p>Loading...</p>}

            {screen === 'offline' && (
                <section className="pixel-box">
                    <p>Can't reach the server.</p>
                    <Button onClick={() => landingScreen().then(setScreen)}>Try again</Button>
                </section>
            )}

            {screen === 'login' && (
                <LoginPage onLogin={handleLogin} onSignUp={() => setScreen('register')} />
            )}

            {screen === 'register' && (
                <RegisterPage
                    initial={draft}
                    onContinue={handleRegisterContinue}
                    onBack={() => setScreen('login')}
                    error={registerError}
                />
            )}

            {screen === 'quiz' && <QuizPage onFinish={handleQuizFinished} />}

            {screen === 'adoption' && <AdoptionPage onAdopted={() => setScreen('game')} />}
        </main>
    )
}

export default App
