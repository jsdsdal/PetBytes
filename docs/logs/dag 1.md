
Vi laver vores projektstruktur.

Indtil videre:

# packages
ai
auth
config
dialogue
interaction
inventory
personality
	/pet
	/user
pet
	/dto
		FeedRequest
		PetResponse
	Pet
	PetController
	PetService
	PetRepository
	PetStateService
shared
simulation
user

---

Vi bygger vores entity op for det centrale af vores spil. Pets.
Her læser jeg mig til at man helst ser wrapper typer fremfor primitive, da de primitive ikke kan være null.
https://thelinuxcode.com/spring-data-jpa-id-annotation-complete-practical-guide-for-production-systems/
Så vi bruger Long Id; hvilket ligner konventionerne.

Vi ville gerne have sit pet har stats. Men vi er ikke så vilde med en fed Pet Entity. Så vi laver en Embeddable.
Heri kan vi også clamp dem til 0-100.

Clamp er noget nyt i java. Korte af det lange er at det lader os blive indenfor en given rækkevidde altid.
https://www.javathinking.com/blog/clamp-function-in-java/

Vi vil gerne sørge for stats altid er mellem 0-100. 

Clamp er bare en one-liner så vi slipper for math max / min.

Som regel har man grænser i db. Men siden spil er state-heavy, så ville vi gerne være benhårde med at tingene opfører sig som vi vil have dem. Derfor tvinger vi det i koden fremfor at sætte en grænse i vores db. Så slipper vi også (forhåbentligt) for den smule extra error handling / validation.

https://www.baeldung.com/jpa-embedded-embeddable

Vi kunne godt tænke os at hver pet har et unikt design. Vi starter med 4. Men for fremtidssikring skaber vi en Species. 
Da vi kan putte attributer på Enum, er de ret oplagte her. Det er også værdier som er faste, og som kun vi som udviklere er interesserede i at rode ved. Så derfor ryger de for sig selv også. med en designkey til sprites.

her er eksemplet fra første udkast.
```java
package org.example.backend.pet.entity;  
  
public enum PetSpecies {  
    CAT("cat", "A small, whiskered creature."),  
    FOX("fox", "A sharp-eared, bushy-tailed creature."),  
    OWL("owl", "A round-eyed, feathered creature."),  
    TURTLE("turtle", "A slow, shelled creature.");  
  
    private final String designKey;  
    private final String description;  
  
    PetSpecies(String designKey, String description) {  
        this.designKey = designKey;  
        this.description = description;  
    }  
  
    public String getDesignKey() { return designKey; }  
}
```

Vores backend skriver fx cat som string. Vores frontend kan forhåbentligt bruge design key til at mappe den korrekte sprite. Vi prøver.
Beskrivelsen er til vores LLM.

Jeg blev desværre ret bidt af en Utility AI.
https://medium.com/@morganwalkupdev/ai-made-easy-with-utility-ai-fef94cd36161

Det kunne være sjovt i projektet. Så nu prøver jeg.

# personligheder
Personligheder har 3 ansvar:

## opførsel
hvor hurtigt bliver de sultne, trætte, osv.
hvor sandsynligt de tager en lur osv.
deres kommunikation overfor spilleren

## preferencer 

hvor mange point de får for hver handling = scoring system
## handlinger
hvad der faktisk sker i øjeblikket
fx vild personlighed = zoomies
hvor en stille personlighed aldrig løber


![[mermaid-diagram.png]]

# postman test

![[Pasted image 20261103215402.png]]

vores entity mapping virker! super fedt.

![[Pasted image 20261103215501.png]]

og den persisterer.
alt er godt

Jeg laver lige et udkast til de 4 monstre vi faktisk vil have til dagen efter
