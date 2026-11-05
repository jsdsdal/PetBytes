# PetBytes

En tamagotchi-inspireret virtuel pet applikation.

Sit kæledyr har persisterende behov, personlighedstræk, og autonom opførsel.
Brugere kan interagere med denne, give den ting og sager, og kommunikere med den.

Nuværende teknologi (TBD):
- **Backend**: Java + Spring Boot
- **Frontend:** React + JavaScript + Vite
- **Database:** MySQL
- **AI Integration:** Gemini API
- **Deployment:** Docker / Docker Compose

Langsigtet retning:
Projektet skal ideelt kunne udvikles separat fra dets individuelle dele, fx Gemini og Spring.
Dvs. delene skal helst undgå coupling til den eksisterende tech stack.

Modsat de projekter vi er vant til, har jeg inddelt packages efter domæne.
hvorfor? -> der er mange bevægende dele, som bliver svære at finde rundt i hvis der pludselig er 50 klasser i "services" fx.
derudover giver det også bedre mental separation af domæner til at overholde nedenstående - netop at hver feature er modulært bygget.

## Arkitektur
Vi hæfter os ved følgende principper
### Separationn of Concerns
De forksellige dele af applikationen skal gerne have egne ansvarsområder.
- Controllers håndterer HTTP forespørgsler og responser
- Services implementerer app og domæne funktioner
- Domæne objekter repræsenterer spil koncepter og regler
- Frontend komponenter håndterer præsentation og bruger interaktion

### Dependency Inversion
Spillets logik skal ikke være afhængigt af anden infrastruktur. Eks:
- Opførsel skal ikke være afhængigt af Spring Boot
- Personlighedskalkulationer skal ikke være afhængige af MySQL
- Dialog skal ikke være alene afhængigt af Gemini
- Spil regler skal ikke være afhængige af HTTP responser.

Dette skal være for at hver del er nemmere at erstatte - specielt AI API.

### Composition over Inheritance
Foretrækker at kombinere selvstændige karakteristikker, attributer og handlinger frem for at skabe mere specialiserede klasse hierarkier.
Et kæledyr har en personligheds profil, personlighed, state osv. i stedet for en subklasse til hver eneste mulige kombination af disse.

## Design patterns

### Strategy pattern
Udskiftelig dialog arkitektur, fx. Gemini kan byttes ud med anden AI (eller scripted dialog)

### Polymorphism
Forskellige implementeringer tilgås via delte interfaces

### Data-driven design
Personligheds profiler og arketyper er konfigureret gennem data fremfor objekter

### Repository pattern
Abstraherer database adgang gennem Spring Data repositories

### DTO pattern
Adskiller API forespørgsler / responser fra persisterede entiteter

### Dependency Inversion
Holde spil logik adskilt og uafhængige af frameworks og eksterne services

### Utility AI
Sit pet kan evaluere og vælge autonome handlinger

## Noter
Hver personlighed behøver ikke sin egen Java implementation.
Hvis personligheder adskiller sig hovedsageligt i parametre (bliver hurtigere træt osv) repræsenteres disse som data.
Separate implementeringer gælder kun når opførsel reelt adskiller sig.

## Personlighed og Utility AI
Personligheder repræsenteres igennem tendenser, ikke forudkodede handlinger.
De 4 personligheder bruges til at påvirke hvilken handling er foretrukken.

## Utility AI
Simulationen evaluerer handlinger ud fra:
- Nuværende behov
- Personlighed modifiers
- Tilgængelige resourcer
- Miljø (er der adgang til at opfylde handlingen ?)

Fx:
- Spis
- Sov
- Lege
- Gå rundt
- Idle

Disse bliver vurderet på hvilken giver det højeste mulige udbytte.
Eksempel:
Dit pet er sulten, men ikke træt, og keder sig heller ikke.
Utility AI lægger alle attributer sammen (sult, energi, glæde) og holder på denne i en beregning til at sammenligne mulige handlinger.
Den vurderer meritten for tilgængelige handlinger ved at udregne hvor meget glæde den får fra hver handling, og rangerer disse.
Så siden den ikke er træt, og heller ikke keder sig, ville legetøj og seng score lavt. 
Mad vil score højst.
Den skal så tjekke om det er muligt at spise.
Hvis ja, så spiser den.

Personlighed her kan så evt. påvirke hvor tilbøjelig den er til visse handlinger ifht. andre.
En som er genert, er måske ikke så tilbøjelig til at lege alene.
En som er vild, er måske ikke så interesseret i at sove.

## Dialog
Dialog er separat fra spillets intelligens.

Spillet regner ud hvad der sker i øjeblikket.
Dialog skal bare regne ud hvad kæledyret har at sige om hvad der er sket.
En delt DialogueProvider interface her med udskiftelige implementeringer:
- GeminiDialogueprovider
- ScriptedDialogueProvider (måske)

Scripted behøver ikke bygges endnu. Den er relevant til en fallback hvis fx. AI API ikke virker, eller man ville fjerne AI helt.

## Modul struktur
- `auth`- autentifikation og JWT
- `user`- konto og bruger info
- `pet` - pet state og lifecycle
- `personality` - personligheds konfiguration, både bruger og pet
- `simulation` - tid, progression og autonom adfærd
- `interaction` - spiller interaktion og historik
- `inventory`- tilgængelige ressourcer for spiller + pet
- `ai` - geminii implemmentation
- `dialogue`- dialog hierarki, logik osv
- `shared` - fælles utilities og delte typer (tbd)

## Guidelines

1. Foretræk simple og forståelige implementeringer over forebyggende abstraktioner til langtidssigtede mål
2. Undgå hardcoding
3. Hold eksterne API nøgler og credentials udenfor source (gemini må altså ikke se andet end hvad der rager den)
4. Brug DTO ved API grænser
5. Spil regler bor i simulation / domæne lag
6. Autonom opførsel skal være observerbar i aktivitetslogs til debugging
7. Dokumenter beslutninger undervejs og hvordan andre moduler skal implementeres efterfølgende
8. Undgå ekstra frameworks medmindre de faktisk løser et problem
   
