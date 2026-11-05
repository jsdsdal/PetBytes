
Java weighted pick:
https://www.baeldung.com/java-compute-weighted-average

Lambda expression til at tage en beslutning med en bias vha generiske funktioner

Java registry class:
https://martinfowler.com/eaaCatalog/registry.html
https://medium.com/@sarathesid/strategy-pattern-with-a-registry-pattern-using-springboot-0aa46b1743e9

Når vi gerne ville have en implementering uden at hænge os på if/else og loops.

Vi har en strategi (personligheder fx) som kan have forskellige inputs, og i stedet for at loope efter at finde de inputs, bruger vi et interface (PersonalityProfileProvider) til at definere de enkelte profiler ved at implementere denne (RambunctiousProfileProvider implements PersonalityProfileProvider)
Vi får altså lov til at holde på variabler et sted og kalde dem et andet uden at lave en masse metoder.

Ideen er:
Spring vil helst have components først.
https://www.baeldung.com/spring-component-scanning
Spring laver en bean ud fra disse:
@Component
RambunctiousProfileProvider

@Service
PersonalityProfileRegistry

Spring ser en constructor som ser sådan ud:
`PersonalityProfileRegistry(List<PersonalityProfileProvider> providers)`

Den ser et interface og vil have alle beans som implementerer dette. På et tidspunkt har vi 4 af disse.

Vores constructor kører så sådan:

```java
providers.stream().collect(toMap( PersonalityProfileProvider::personality, // key: RAMBUNCTIOUS
PersonalityProfileProvider::profile)) // value: the PersonalityProfile
→ Map { RAMBUNCTIOUS → PersonalityProfile(1.5, 0.6, 1.8, ...) }
```

hver gang vi skal bruge en profil, kører vi med:
```java
`registry.getProfile(pet.getPersonality()) // lookup
```

Det kaldes også constructor injection.
links:
https://www.baeldung.com/spring-injecting-collections
https://medium.com/codex/implementing-the-strategy-design-pattern-in-spring-boot-df3adb9ceb4a
https://james-storr.co.uk/article/66aceece4edeb3fdb86a4d0e

Spring råder også selv til constructor injection fremfor autowired
https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html


Her: 
https://www.youtube.com/watch?v=XgS6aazp11M
Han beskriver også hvordan man kan lave spil i Spring. Bl.a vha. beans i stor stil.

Spring bliver åbenbart også brugt i gambling.
Dette giver mening, da Spring lader udvikleren bestemme en masse ting fra server-siden uden at klienten behøver at være til stede / responsiv. 
Vi prøver!


Spring @Component, @Service, og @Repository: </br>
https://www.javathinking.com/blog/component-vs-repository-and-service-in-spring/#component
