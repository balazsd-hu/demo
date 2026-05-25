package hu.nye.demo.service;

import hu.nye.demo.model.Munkavallalo;
import hu.nye.demo.repository.MunkavallaloRepository;
import hu.nye.demo.repository.FoglalasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MunkavallaloService {

    private final MunkavallaloRepository munkavallaloRepository;
    private final FoglalasRepository foglalasRepository; // ÚJ BEKÖTÉS

    public MunkavallaloService(MunkavallaloRepository munkavallaloRepository, FoglalasRepository foglalasRepository) {
        this.munkavallaloRepository = munkavallaloRepository;
        this.foglalasRepository = foglalasRepository;
    }

    public List<Munkavallalo> osszesMunkavallalo() {
        return munkavallaloRepository.findAll();
    }

    public Munkavallalo keresesIdAlapjan(Long id) {
        return munkavallaloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Munkavállaló nem található: " + id));
    }

    public Munkavallalo mentés(Munkavallalo m) {
        return munkavallaloRepository.save(m);
    }

    @Transactional // Kötelező, mert az adatbázisban több táblát is módosítunk egyszerre!
    public void torles(Long id) {
        System.out.println(">> BACKEND: Szakember törlése indítva (ID: " + id + ")");

        foglalasRepository.deleteByMunkavallaloId(id);

        munkavallaloRepository.deleteById(id);

        System.out.println(">> BACKEND: Szakember és foglalásai sikeresen törölve.");
    }
}