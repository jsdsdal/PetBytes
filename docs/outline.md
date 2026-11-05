tamagotchi app som taler igen

---
# opgavekrav
>Individuel eller gruppeprojekt. Du/I skabe en kreativ AI-baseret ide og implementere løsning baseret på de teknologier og discipliner i har lært.

Opgaven er meget åben til fortolkning og kræver ikke andet end at vi henter fra en REST API og bruger en AI i en eller anden form til at tolke dette data fra backenden. 

Der er ingen krav om hvad det er eller hvordan dette virker.
# tools
- Spring AI med Gemini (gratis key)
- JWT-authenticated REST API (vi laver denne selv ?)
- render til hosting ? eller min egen vm. dont know
- React components 
- evt relationel db til at holde på saves osv

vi låner i stor stil fra dette:
https://github.com/goldlabelapps/tamagotchi

og i stedet for python bruger vi blot vores java stack

# user flow

du opretter din konto hvor din tamagocthi bliver oprettet
derefter henter vi fra en backend til at finde den tamagotchi du starter med. denne bliver tegnet med ASCII formentligt
google gemini giver tamagotchi sin "stemme" så du kan snakke med den. den giver dynamisk feedback afhængigt af om den er sulten, om den keder sig, osv.

måske laver jeg en meget basic utility AI ? 
så når du ikke aktivt tjekker din tamagotchi, kan den selv gå til skålen (givet du har fyldt denne) og få mad. 

# breakdown - PetBytes:

- Personlighedstest i begyndelsen af spillet. Det her bestemmer din brugers "arketype" - a la Pokemon Mystery Dungeon. Formentlig enum. Vigtigt disse kan udvides. Basis for hvordan kæledyr taler til dig *og* hvilket slags kæledyr du sandsynligvis ender med.
- Dinn bruger oprettes. Den har password + brugernavn og email. Til at følge save states + øves i spring security.
- Efter personlighedstest får du tilsvarende kæledyr. Første iteration / MVP har 4 forskellige personligheder for brugeren og 4 tilsvarende for pets. disse skal også kunne udvides.
- Pet har et meget basic utility-based AI. Kan fodre sig selv og gå i seng osv. selvstændigt uanset om dit vindue er åbent eller ej. Appen kører i baggrunden så længe den bor på en server uafbrudt. (Spring scheduler ? Og/eller bruger timestamps til at udregne udfald a hændelser siden sidste login. Skal undersøges)
- Gameplay er lånt meget fra tamagotchi. Modsat tamagotchi (som bare dør) kan den her faktisk være en anelse selvstændigt, givet du giver den midlerne til at passe sig selv.
- Pet låner Gemini AI (gratis API) og fetcher fra REST APIen (laves selv) for at interagere med spilleren. Måske kan der vejes så et pet du ikke passer så godt er lidt sur, en glad taler meget til dig osv. Vigtigt - du taler ALDRIG selv til den. Den plaprer bare løst lidt skizofrent. Mest for at der ikke skal tænkes på gdpr osv.
- Den kan blive ældre. 3 stadier for nu. Barn -> ung -> voksen. AI bruger også dette til kontekst. Visuelt gøres den bare lille, mellem stor uden vanvittige ændringer.

Project structure:
Mono repo featuring:
Backend -> Spring Boot, Gemini AI, MySQL database. incl. REST API vi selv bygger
Frontend -> Vite + React + Node.js. Fetcher fra backend.

# Personality

| User (how a person is) | Pet (how a creature behaves) |
| ---------------------- | ---------------------------- |
| ANALYTICAL             | CONTEMPLATIVE                |
| CAREGIVER              | AFFECTIONATE                 |
| SPONTANEOUS            | RAMBUNCTIOUS                 |
| METICULOUS             | SKITTISH                     |

# links 
https://github.com/Amheisern/TamagotchiAPI
https://github.com/goldlabelapps/tamagotchi
https://docs.spring.io/spring-framework/reference/data-access/dao.html
https://www.geeksforgeeks.org/java/how-to-create-a-rest-api-using-java-spring-boot/
https://github.com/kerrbrittany9/tamagotchi-react
