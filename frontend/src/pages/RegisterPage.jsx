import { useState} from "react";
import { Button } from '@/components/ui/pixelact-ui/button'
import { Input } from '@/components/ui/pixelact-ui/input'
import { Label } from '@/components/ui/pixelact-ui/label'
import { login } from '@/api/userApi.js'
import {PERSONALITIES} from "@/data/quizQuestions.js";

function RegisterPage({ onContinue, onBack, error }) {
    const [username, setUsername] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")

    async function handleSubmit(event) {
        event.preventDefault()
        onContinue({ username, email, password})
    }

    return (
        <form className="pixel-box" onSubmit={handleSubmit}>
            <h2>Create account</h2>

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
                    Email
                    <Input
                        required
                        type="email"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
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

                <Button type="submit">Sign up</Button>
                <Button type="button" variant="link" onClick={onBack}>Back to log in</Button>

            </fieldset>
        </form>
    )
}
export default RegisterPage