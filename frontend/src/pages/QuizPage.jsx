import { useState} from "react";
import { Button } from '@/components/ui/pixelact-ui/button'
import {PERSONALITIES, QUIZ_QUESTIONS} from "@/data/quizQuestions.js";

const START_SCORES = { ANALYTICAL: 0, CAREGIVER: 0, SPONTANEOUS: 0, METICULOUS: 0}

function QuizPage() {
    const [currentIndex, setCurrentIndex] = useState(0)
    const [scores, setScores] = useState(START_SCORES)


    function findWinner(scores) {
        let winner = 'ANALYTICAL'
        for (const personality in scores) {
            if (scores[personality] > scores[winner]) {
                winner = personality
            }
        }
        return winner
    }

    function handleAnswer(answer) {
        setScores({
            ...scores, [answer.personality]: scores[answer.personality] + 1,
        })
        setCurrentIndex(currentIndex + 1)
    }

        if (currentIndex >= QUIZ_QUESTIONS.length) {
            const winner = findWinner(scores)
            const result = PERSONALITIES[winner]

            return (
                <section className="pixel-box">
                    <h2>You are {result.name}!</h2>
                    <p>{result.description}</p>
                </section>
            )
        }

        const question = QUIZ_QUESTIONS[currentIndex]

    return (
        <section className="pixel-box">
            <p className="muted">Question {currentIndex + 1} of {QUIZ_QUESTIONS.length}</p>
            <h2>{question.question}</h2>

            <div className="stack">
                {question.answers.map((answer) => (
                    <Button key={answer.text} onClick={() => handleAnswer(answer)}>
                        {answer.text}
                    </Button>
                ))}
            </div>
        </section>
    )
    }

    export default QuizPage