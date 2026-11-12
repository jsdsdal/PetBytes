import { useState} from "react";
import { Button } from '@/components/ui/pixelact-ui/button'
import { Input } from '@/components/ui/pixelact-ui/input'
import { Label } from '@/components/ui/pixelact-ui/label'
import { login } from '@/api/userApi.js'

function LoginPage({ onLogin, onSignUp }) {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")

    async function handleSubmit(event) {
        event.preventDefault()
        setError("")

        try {
            await login(username,password)
            onLogin(username)
        } catch (err) {
            setError(err.message)
        }
    }
    return (
        <form className="pixel-box" onSubmit={handleSubmit}>
            <h2>Log in</h2>

            <fieldset className="stack">
                <Label className="field">
                    Username
                    <Input
                        required
                        value={username}
                        onChange={(event) => setUsername(event.target.value)}
                    />
                </Label>

                <Label className="field">
                    Password
                    <Input
                        required
                        type="password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                    />
                </Label>

                {error && <p className="error">{error}</p>}

                <Button type="submit">Log in</Button>
                <Button type="button" variant="link" onClick={onSignUp}>No account? Sign up</Button>

            </fieldset>
        </form>
    )
}
export default LoginPage