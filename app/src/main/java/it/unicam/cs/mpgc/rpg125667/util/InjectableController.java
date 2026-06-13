package it.unicam.cs.mpgc.rpg125667.util;

import it.unicam.cs.mpgc.rpg125667.repository.*;

public interface InjectableController {
    
    void setRepository(IPlayerRepository repository);
}