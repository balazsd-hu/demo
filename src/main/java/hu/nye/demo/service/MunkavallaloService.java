package hu.nye.demo.service;

import hu.nye.demo.model.Munkavallalo;
import hu.nye.demo.repository.MunkavallaloRepository;
import hu.nye.demo.repository.FoglalasRepository; // ÚJ IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ÚJ IMPORT

import java.util.List;

@Service
public class MunkavallaloService {

    private final MunkavallaloRepository munkavallaloRepository;
    private final FoglalasRepository foglalasRepository; // ÚJ BEKÖTÉS

    // A Spring ide is bepakolja mindkét repository-t automatikusan
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

    // 🔥 ÁTÍRT, GOLYÓÁLLÓ TÖRLÉS METÓDUS
    @Transactional // Kötelező, mert az adatbázisban több táblát is módosítunk egyszerre!
    public void torles(Long id) {
        System.out.println(">> BACKEND: Szakember törlése indítva (ID: " + id + ")");

        // 1. LÉPÉS: Először kisöpörjük a hozzá kapcsolódó foglalásokat a háttértáblából
        foglalasRepository.deleteByMunkavallaloId(id);

        // 2. LÉPÉS: Most már biztonságosan törölhetjük magát a szakembert, nem lesz Foreign Key hiba
        munkavallaloRepository.deleteById(id);

        System.out.println(">> BACKEND: Szakember és foglalásai sikeresen törölve.");
    }
}