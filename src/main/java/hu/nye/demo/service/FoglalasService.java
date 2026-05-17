package hu.nye.demo.service;

import hu.nye.demo.model.Foglalas;
import hu.nye.demo.model.Felhasznalo;
import hu.nye.demo.model.Munkavallalo;
import hu.nye.demo.repository.FoglalasRepository;
import hu.nye.demo.repository.FelhasznaloRepository;
import hu.nye.demo.repository.MunkavallaloRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoglalasService {

    private final FoglalasRepository foglalasRepository;
    private final FelhasznaloRepository felhasznaloRepository;
    private final MunkavallaloRepository munkavallaloRepository;

    public FoglalasService(FoglalasRepository foglalasRepository,
                           FelhasznaloRepository felhasznaloRepository,
                           MunkavallaloRepository munkavallaloRepository) {
        this.foglalasRepository = foglalasRepository;
        this.felhasznaloRepository = felhasznaloRepository;
        this.munkavallaloRepository = munkavallaloRepository;
    }

    @Transactional
    public Foglalas lefoglal(Long felhasznaloId, Long munkavallaloId) {
        Felhasznalo felhasznalo = felhasznaloRepository.findById(felhasznaloId)
                .orElseThrow(() -> new IllegalArgumentException("Felhasználó nem található"));

        Munkavallalo munkavallalo = munkavallaloRepository.findById(munkavallaloId)
                .orElseThrow(() -> new IllegalArgumentException("Munkavállaló nem található"));

        if (!munkavallalo.isElerheto()) {
            throw new IllegalStateException("Ez a munkavállaló már foglalt!");
        }

        munkavallalo.setElerheto(false);
        munkavallaloRepository.save(munkavallalo);

        Foglalas foglalas = new Foglalas();
        foglalas.setFelhasznalo(felhasznalo);
        foglalas.setMunkavallalo(munkavallalo);

        // Idők beállítása
        foglalas.setFoglalasIdopontja(LocalDateTime.now());
        // TESZTELÉSHEZ: 1 perc múlva lejár (Élesben: .plusHours(2) lesz)
        foglalas.setLejaratIdopontja(LocalDateTime.now().plusMinutes(30));

        return foglalasRepository.save(foglalas);
    }

    // TAKARÍTÓ ROBOT: 10 másodpercenként átnézi a táblát a háttérben
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void lejartFoglalasokTakaritasa() {
        List<Foglalas> osszesFoglalas = foglalasRepository.findAll();
        LocalDateTime most = LocalDateTime.now();

        for (Foglalas f : osszesFoglalas) {
            // Ha a lejárati idő régebbi mint a mostani idő, és a munkás még mindig foglaltként van elmentve
            if (f.getLejaratIdopontja() != null && f.getLejaratIdopontja().isBefore(most) && !f.getMunkavallalo().isElerheto()) {

                System.out.println(">> AUTOMATIZÁCIÓ: " + f.getMunkavallalo().getNev() + " foglalása lejárt! Felszabadítás...");

                // 1. Munkavállaló visszaállítása elérhetőre
                Munkavallalo m = f.getMunkavallalo();
                m.setElerheto(true);
                munkavallaloRepository.save(m);

                // 2. A lejárt foglalási papír törlése a raktárból
                foglalasRepository.delete(f);
            }
        }
    }

    public List<Foglalas> osszesFoglalas() {
        return foglalasRepository.findAll();
    }
}