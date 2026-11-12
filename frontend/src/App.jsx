import './App.css'
import { useState } from 'react'
import QuizPage from "@/pages/QuizPage.jsx";
import LoginPage from "@/pages/LoginPage.jsx";
import RegisterPage from "@/pages/RegisterPage.jsx";
import {login, logout, register} from "@/api/userApi.js";
import {Button} from "@/components/ui/pixelact-ui/button.tsx";
import { getMyPets } from './api/petApi'
import AdoptionPage from "@/pages/AdoptionPage.jsx"

function App() {
    const [screen, setScreen] = useState(sessionStorage.getItem('token') ? 'game' : 'login')
    const [user, setUser] = useState(sessionStorage.getItem('username'))
    const [newUser, setNewUser] = useState(null)
    const [registerError, setRegisterError] = useState("")

    function handleLogin(username) {
        setUser(username)
        setScreen("game")
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

    async function handleQuizFinished(personality) {
        try {
            await register(newUser.username, newUser.email, newUser.password, personality)
            await login(newUser.username, newUser.password)
            handleLogin(newUser.username)
        }catch (err) {
            setRegisterError(err.message)
            setScreen("register")
        }
    }

  return (
      <main className="page">
        <h1>PetBytes</h1>
          {/* Kun ÉN af disse linjer er sand ad gangen, så kun én side vises.
          && betyder "vis kun hvis det til venstre er sandt". */}

          {screen === 'login' && (
              <LoginPage onLogin={handleLogin} onSignUp={() => setScreen("register")} />
          )}

          {screen === "register" && (
              <RegisterPage onContinue={handleRegisterContinue} onBack={() => setScreen("login")} error={registerError}
          />
          )}

          {screen === 'quiz' && <QuizPage onFinish={handleQuizFinished} />}

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
                  </div>
              </section>
          )}

      </main>
  )
}

export default App