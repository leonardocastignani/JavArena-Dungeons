package it.unicam.cs.mpgc.rpg125667;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;
import java.util.*;

public class App {

    public static void main(String[] args) {
        System.out.println("Avvio connessione al Database...");

        PlayerRepository repository = new PlayerRepository();

        CharacterStats stats = new CharacterStats(100, 100, 15, 5);
        Player hero = new Player("Artù", stats);

        System.out.println("Salvataggio di " + hero.getName() + " in corso...");
        repository.save(hero);
        System.out.println("Salvataggio completato! ID assegnato: " + hero.getId());

        Optional<Player> loadedHeroOpt = repository.findById(hero.getId());
        
        if (loadedHeroOpt.isPresent()) {
            Player loadedHero = loadedHeroOpt.get(); // Estraiamo il giocatore
            System.out.println("Giocatore ricaricato con successo:");
            System.out.println("- Nome: " + loadedHero.getName());
            System.out.println("- HP Attuali: " + loadedHero.getCurrentHealth());
        } else {
            System.out.println("Errore: Giocatore non trovato nel database!");
        }

        repository.close();
        System.out.println("Test concluso. Controlla la cartella app/src/data!");
    }
}