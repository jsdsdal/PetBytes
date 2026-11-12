import './App.css'
import { useState, useEffect } from 'react'
import QuizPage from "@/pages/QuizPage.jsx";
import LoginPage from "@/pages/LoginPage.jsx";
import RegisterPage from "@/pages/RegisterPage.jsx";
import AdoptionPage from "@/pages/AdoptionPage.jsx";
import GamePage from "@/pages/GamePage.jsx";
import { login, logout, register } from "@/api/userApi.js";
import { getMyPets } from './api/petApi'

function App() {
    const [screen, setScreen] = useState('loading')                // CHANGED: was a ternary
    const [user, setUser] = useState(sessionStorage.getItem('username'))
    const [newUser, setNewUser] = useState(null)
    const [registerError, setRegisterError] = useState("")

    // kører ved start / reload
    useEffect(() => {
        if (!sessionStorage.getItem('token')) {
            setScreen('login')
            return
        }
        resolveLandingScreen().then(setScreen)
    }, [])

    // hjælper
    async function resolveLandingScreen() {
        try {
            const pets = await getMyPets()
            return pets.length > 0 ? 'game' : 'adoption'
        } catch {
            // token stale/invalid → start fresh
            logout()
            return 'login'
        }
    }

    // async, router afhængigt om de skal have et pet
    async function handleLogin(username) {
        setUser(username)
        setScreen(await resolveLandingScreen())
    }

    function handleLogout() {
        logout()
        setUser(null)
        setScreen("login")
    }

    function handleRegisterContinue(formData) {
        setNewUser(formData)
        setRegisterError('')
        setScreen('quiz')
    }

    // går direkte til adoption nu
    async function handleQuizFinished(personality) {
        try {
            await register(newUser.username, newUser.email, newUser.password, personality)
            await login(newUser.username, newUser.password)
            setUser(newUser.username)
            setScreen('adoption')
        } catch (err) {
            setRegisterError(err.message)
            setScreen("register")
        }
    }

    return (
        <main className="page">
            <h1>PetBytes</h1>

            {/*loading*/}
            {screen === 'loading' && <p>Loading...</p>}

            {/* loginskærm */}
            {screen === 'login' && (
                <LoginPage onLogin={handleLogin} onSignUp={() => setScreen("register")} />
            )}

            {/* registrer */}
            {screen === "register" && (
                <RegisterPage
                    onContinue={handleRegisterContinue}
                    onBack={() => setScreen("login")}
                    error={registerError}
                />
            )}

            {/* quizflow */}
            {screen === 'quiz' && <QuizPage onFinish={handleQuizFinished} />}

            {/* adoption */}
            {screen === "adoption" && (
                <AdoptionPage onAdopted={() => setScreen("game")} />
            )}

          {/* Spillet laver vi i næste trin. Indtil da en velkomst. */}
          {screen === 'game' && (
              <section className="pixel-box">
                  <p>Welcome, {user}!</p>
                  <div className="stack">
                      {/* Henter dine dyr med tokenen og skriver dem i konsollen */}
                      <Button onClick={async () => console.log(await getMyPets())}>Test token</Button>
                      <Button variant="link" onClick={handleLogout}>Log out</Button>
                      {/* MIDLERTIDIGT: test af StatBar med faste tal */}
                      <StatBar label="Food" value={80} />
                      <StatBar label="Happy" value={40} />
                      <StatBar label="Energy" value={10} />
                  </div>
              </section>
          )}

      </main>
  )
}

export default App