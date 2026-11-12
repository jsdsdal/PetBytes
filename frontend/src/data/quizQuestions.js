export const QUIZ_QUESTIONS = [
    {
        question: "What word best describes you?",
        answers: [
            {text: "Curious", personality:'ANALYTICAL'},
            {text: "Caring", personality:'CAREGIVER'},
            {text: "Adventurous", personality:'SPONTANEOUS'},
            {text: "Thorough", personality:'METICULOUS'},
        ],
    },

    {
        question: "Your pet starts speaking, how would you react?",
        answers: [
            {text: "Stay calm and try to figure out what's going on", personality:'ANALYTICAL'},
            {text: "Sit down and listen to what it has to say", personality:'CAREGIVER'},
            {text: "Scream, then immediately start chatting", personality:'SPONTANEOUS'},
            {text: "Write down everything it says, word for word", personality:'METICULOUS'},
        ],
    },

    {
        question: "What kind of instrument most pleases your ear?",
        answers: [
            {text: 'The violin', personality:'ANALYTICAL'},
            {text: 'The piano', personality:'CAREGIVER'},
            {text: 'The trumpet', personality:'SPONTANEOUS'},
            {text: 'The drums', personality:'METICULOUS'},
        ],
    },

    {
        question: "You enter an enchanted garden. What would you be most curious to examine first?",
        answers: [
            {text: 'The statue of an old wizard with a strangely twinkling eye', personality:'ANALYTICAL'},
            {text: 'The fat red toadstools that appear to be talking to each other', personality:'CAREGIVER'},
            {text: 'The bubbling pool, in the depths of which something luminous is swirling', personality:'SPONTANEOUS'},
            {text: 'The silver leafed tree bearing golden apples', personality:'METICULOUS'},
        ],
    },
]


export const PERSONALITIES = {
    ANALYTICAL: { name: 'Analytical', description: 'Logical and methodical. Drawn to understanding how things work.' },
    CAREGIVER: { name: 'Caregiver', description: 'Empathetic and nurturing. Drawn to helping others.' },
    SPONTANEOUS: { name: 'Spontaneous', description: 'Impulsive and curious. Drawn to novelty and action.' },
    METICULOUS: { name: 'Meticulous', description: 'Precise and careful. Drawn to order and correctness.' },
}

