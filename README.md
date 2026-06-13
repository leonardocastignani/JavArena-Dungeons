# 📌 JavArena Dungeons

**JavArena Dungeons** è un videogioco RPG (Gioco di Ruolo) a turni con interfaccia grafica sviluppato in Java e JavaFX. Il giocatore può creare eroi personalizzati, affrontare mostri generati proceduralmente in un'arena, accumulare esperienza per salire di livello e gestire i propri progressi attraverso un sistema di persistenza automatica su file JSON.

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- **Java 25 (LTS)** (Il progetto utilizza feature moderne come i `Record` e richiede una versione LTS stabile).
- **Gradle 9.5.1** (incluso tramite wrapper).

### Istruzioni

Clona il repository sul tuo computer in una cartella a tua scelta:
```bash
git clone <url-del-tuo-repository-github>
cd JavArenaDungeons
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

### 🤖 Uso di strumenti di AI
Durante lo sviluppo di questo progetto è stato fatto un uso ragionato, consapevole e didattico di strumenti di Intelligenza Artificiale (Gemini), impiegati con il ruolo di "Senior Developer / Code Reviewer".

L'AI non è stata usata per farsi scrivere il progetto da zero, ma come supporto per:
- **Comprendere concetti teorici avanzati**, come la differenza tra l'accoppiamento forte nei Controller e la Dependency Injection nativa di JavaFX.
- **Migliorare l'aderenza ai principi S.O.L.I.D.**, venendo guidato nel disaccoppiamento della UI dalla logica di Dominio tramite l'introduzione di un Service Layer.
- **Analizzare vulnerabilità nel codice**, ricevendo suggerimenti per correggere problemi di concorrenza (Race Condition) e ottimizzare le performance (implementazione di una View Cache).
- **Supporto per la documentazione**, per apprendere la corretta sintassi e il livello di dettaglio richiesto dallo standard JavaDoc accademico.

Tutto il codice generato in seguito ai suggerimenti dell'AI è stato analizzato, modificato, adattato alle specifiche universitarie e testato personalmente prima dell'integrazione finale.

📌 Per una dichiarazione estremamente dettagliata dei refactoring applicati grazie all'AI e delle logiche architetturali apprese, si prega di fare riferimento alla Wiki del repository.
