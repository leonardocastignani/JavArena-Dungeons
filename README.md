# ⚔️ JavArena Dungeons

**JavArena Dungeons** è un videogioco RPG (Gioco di Ruolo) a turni con interfaccia grafica sviluppato in Java e JavaFX. Il giocatore può creare eroi personalizzati, affrontare mostri generati proceduralmente in un'arena, accumulare esperienza per salire di livello e gestire i propri progressi attraverso un sistema di salvataggio manuale su file JSON.

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- **Java 25 (LTS)**
- **Gradle 9.5.1**

### Istruzioni

Clona il repository sul tuo computer in una cartella a tua scelta:
```bash
git clone https://github.com/leonardocastignani/JavArena-Dungeons.git
cd JavArena-Dungeons
```

### Build del progetto

Per compilare il progetto e scaricare automaticamente tutte le dipendenze necessarie, esegui:
```bash
./gradlew build
```

### Esecuzione

Per lanciare l'applicazione desktop, utilizza il comando:
```bash
./gradlew run
```

## 🎮 Funzionalità Principali
* **Creazione Personaggio:** Crea il tuo eroe unico.
* **Combat System a Turni:** Sistema tattico che include probabilità di schivata, colpi critici e utilizzo di oggetti (pozioni).
* **Permadeath:** Se i tuoi HP scendono a zero, il salvataggio viene eliminato in modo permanente e asincrono.
* **Generazione Procedurale (Sliding Window):** I mostri si adattano al livello del giocatore. Non incontrerai mai nemici troppo deboli o impossibili da battere.
* **Salvataggio Manuale:** I progressi (Esperienza, Livello, HP residui) vengono serializzati su disco in formato JSON.

## 🏗️ Architettura e Pattern (Progetto Universitario)
Il progetto è stato refattorizzato ponendo forte enfasi sull'Ingegneria del Software e sui principi **SOLID**:
* **MVC Pattern:** Netta separazione tra View (FXML), Controller (Java) e Model (Logica di business).
* **Strategy Pattern (`CombatAction`):** Il motore di combattimento delega la risoluzione delle azioni (Attacco, Cura) a classi esterne, rispettando l'Open/Closed Principle.
* **Abstract Factory / Dependency Injection (`MonsterGenerator`):** La generazione dei mostri è separata dal caricamento su disco (I/O), garantendo testabilità e il Single Responsibility Principle.
* **Strategy Pattern (`RewardCalculator`):** Il calcolo delle ricompense di fine battaglia (XP, level-up) è isolato in una strategia dedicata, disaccoppiata da `BattleEngine` e sostituibile senza modificarne il codice.
* **Stream API:** Uso mirato di programmazione funzionale per il filtraggio dei mostri idonei al livello del giocatore (`RandomMonsterGenerator`) e la ricerca di eroi salvati per nome (`CharacterCreationController`).
* **Multithreading:** Scrittura su database JSON delegata a un thread pool asincrono per non bloccare il JavaFX Application Thread.

### 🤖 Uso di strumenti di AI
Durante lo sviluppo di questo progetto è stato fatto un uso ragionato, consapevole e didattico di strumenti di Intelligenza Artificiale (Gemini), impiegati con il ruolo di "Senior Developer / Code Reviewer".

L'AI non è stata usata per farsi scrivere il progetto da zero, ma come supporto per:
- **Comprendere concetti teorici avanzati**, come la differenza tra l'accoppiamento forte nei Controller e la Dependency Injection nativa di JavaFX.
- **Migliorare l'aderenza ai principi S.O.L.I.D.**, venendo guidato nel disaccoppiamento della UI dalla logica di Dominio tramite l'introduzione di un Service Layer.
- **Analizzare vulnerabilità nel codice**, ricevendo suggerimenti per correggere problemi di concorrenza (Race Condition) e ottimizzare le performance (implementazione di una View Cache).
- **Supporto per la documentazione**, per apprendere la corretta sintassi e il livello di dettaglio richiesto dallo standard JavaDoc accademico.

Tutto il codice generato in seguito ai suggerimenti dell'AI è stato analizzato, modificato, adattato alle specifiche universitarie e testato personalmente prima dell'integrazione finale.

### 📚 Documentazione
Per una dichiarazione estremamente dettagliata dei refactoring applicati grazie all'AI e delle logiche architetturali apprese, si prega di fare riferimento alla Wiki del repository, organizzata nelle seguenti pagine tematiche:
- [**Architettura e Design**](https://github.com/leonardocastignani/JavArena-Dungeons/wiki/Architettura-e-Design)
- [**Classi Principali**](https://github.com/leonardocastignani/JavArena-Dungeons/wiki/Classi-Principali)
- [**Estendibilità**](https://github.com/leonardocastignani/JavArena-Dungeons/wiki/Estendibilita)
- [**Persistenza e Concorrenza**](https://github.com/leonardocastignani/JavArena-Dungeons/wiki/Persistenza-e-Concorrenza)
- [**SOLID e Design Pattern**](https://github.com/leonardocastignani/JavArena-Dungeons/wiki/SOLID-e-Design-Pattern)
- [**Uso dell'AI**](https://github.com/leonardocastignani/JavArena-Dungeons/wiki/Uso-dell-AI)