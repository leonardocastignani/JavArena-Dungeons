# ⚔️ JavArena Dungeons

**JavArena Dungeons** è un videogioco RPG (Gioco di Ruolo) a turni con interfaccia grafica sviluppato in Java e JavaFX. Il giocatore può creare eroi personalizzati, affrontare mostri generati proceduralmente in un'arena, accumulare esperienza per salire di livello e gestire i propri progressi attraverso un sistema di salvataggio manuale su database H2 tramite JPA/Hibernate.

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

Il repository include già il database H2 (`app/data/saves/JavArenaDungeonsDB.mv.db`) con due eroi pronti da caricare dalla schermata "Carica Partita": uno che ha già raggiunto il livello massimo (traguardo di vittoria) e uno a metà progressione, per poter esplorare subito entrambi gli scenari senza dover giocare da zero.

## 🎮 Funzionalità Principali
* **Creazione Personaggio:** Crea il tuo eroe unico, con nome personalizzato e statistiche iniziali bilanciate.
* **Combat System a Turni:** Sistema tattico con probabilità di schivata (10%), colpi critici (10%, danno raddoppiato) e utilizzo di oggetti (pozioni curative, +30% HP massimi).
* **Continuazione Fluida:** Dopo una vittoria puoi affrontare subito la battaglia successiva con lo stesso eroe, senza dover salvare e ricaricare la partita.
* **Vittoria di Partita:** Al completamento della progressione (livello 5 con barra XP piena) l'eroe raggiunge un traguardo celebrativo una tantum, restando comunque pienamente giocabile in seguito.
* **Permadeath:** Se i tuoi HP scendono a zero, il salvataggio viene eliminato in modo permanente.
* **Salvataggi Multipli:** La schermata "Carica Partita" elenca tutti gli eroi salvati con data dell'ultimo salvataggio e pannello dettagli (livello, HP, XP, statistiche, pozioni), segnalando con il suffisso "MAX" chi ha già raggiunto il livello massimo.
* **Generazione Procedurale (Sliding Window):** I mostri si adattano al livello del giocatore. Non incontrerai mai nemici troppo deboli o impossibili da battere.
* **Salvataggio Manuale:** I progressi (Esperienza, Livello, HP residui, Pozioni) vengono persistiti su un database H2 embedded tramite JPA/Hibernate.

## 🏗️ Architettura e Pattern (Progetto Universitario)
Il progetto è stato refattorizzato ponendo forte enfasi sull'Ingegneria del Software e sui principi **SOLID**:
* **MVC Pattern:** Netta separazione tra View (FXML), Controller (Java) e Model (Logica di business).
* **Strategy Pattern (`CombatAction`):** Il motore di combattimento delega la risoluzione delle azioni (Attacco, Cura) a classi esterne, rispettando l'Open/Closed Principle.
* **Abstract Factory / Dependency Injection (`MonsterGenerator`):** La generazione dei mostri è separata dal caricamento su disco (I/O), garantendo testabilità e il Single Responsibility Principle.
* **Strategy Pattern (`RewardCalculator`):** Il calcolo delle ricompense di fine battaglia (XP, level-up) è isolato in una strategia dedicata, disaccoppiata da `BattleEngine` e sostituibile senza modificarne il codice.
* **Strategy Pattern + Dependency Inversion (`BattleOutcomeHandler`):** La risoluzione degli esiti di fine battaglia (sconfitta, vittoria di partita, prosecuzione) è delegata a una catena di strategie dedicate, ciascuna in una classe propria e nominata. Aggiungere una nuova regola di progressione richiede solo una nuova classe registrata nella catena, senza toccare `ArenaController`; la comunicazione verso la UI passa dall'interfaccia `BattleOutcomeView`, così che il package `engine` resti privo di dipendenze da JavaFX, coerentemente con `MonsterGenerator` e `RewardCalculator`.
* **Stream API:** Uso mirato di programmazione funzionale per il filtraggio dei mostri idonei al livello del giocatore (`RandomMonsterGenerator`) e la ricerca di eroi salvati per nome (`CharacterCreationController`).
* **Persistenza Transazionale (JPA/Hibernate):** Il salvataggio è delegato a un database H2 embedded tramite `HibernatePlayerRepository`, senza cache applicativa manuale da tenere sincronizzata: la coerenza tra dato persistito e oggetto `Player` è garantita direttamente dalle transazioni JPA.

## 📂 Struttura del Progetto
```
it.unicam.cs.mpgc.rpg125667/
├── controller/     # Controller JavaFX (uno per schermata/FXML)
├── engine/         # Motore di combattimento, generazione mostri e calcolo ricompense
│   ├── action/     # Strategy pattern per le azioni di combattimento
│   ├── generator/  # Abstract Factory per la generazione dei mostri
│   └── outcome/    # Strategy pattern per gli esiti di fine battaglia (vittoria/sconfitta/prosecuzione)
├── model/          # Entità di dominio (Player, Monster, CharacterStats...)
├── repository/     # Layer di persistenza (HibernatePlayerRepository)
├── service/        # Service layer che disaccoppia i Controller dalla persistenza
└── util/           # Utility trasversali (SceneManager, GameConfig...)
```

### 🤖 Uso di strumenti di AI
Durante lo sviluppo di questo progetto è stato fatto un uso ragionato, consapevole e didattico di strumenti di Intelligenza Artificiale (Gemini, Claude), impiegati con il ruolo di "Senior Developer / Code Reviewer".

L'AI non è stata usata per farsi scrivere il progetto da zero, ma come supporto per:
- **Comprendere concetti teorici avanzati**, come la differenza tra l'accoppiamento forte nei Controller e la Dependency Injection nativa di JavaFX.
- **Migliorare l'aderenza ai principi S.O.L.I.D.**, venendo guidato nel disaccoppiamento della UI dalla logica di Dominio tramite l'introduzione di un Service Layer.
- **Analizzare vulnerabilità nel codice**, ricevendo suggerimenti per correggere problemi di concorrenza (Race Condition) e ottimizzare le performance (implementazione di una View Cache).
- **Progettare e implementare la migrazione della persistenza** da file JSON a database H2 embedded tramite JPA/Hibernate, con relativa verifica end-to-end del comportamento transazionale.
- **Migliorare l'estendibilità del motore di gioco**, estraendo la risoluzione degli esiti di battaglia in una catena di strategie dedicate (`BattleOutcomeHandler`) al posto di una sequenza di condizioni annidate nel controller.
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
