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

        foglalas.setFoglalasIdopontja(LocalDateTime.now());

        foglalas.setLejaratIdopontja(LocalDateTime.now().plusMinutes(30));

        return foglalasRepository.save(foglalas);
    }


    @Scheduled(fixedRate = 10000)
    @Transactional
    public void lejartFoglalasokTakaritasa() {
        List<Foglalas> osszesFoglalas = foglalasRepository.findAll();
        LocalDateTime most = LocalDateTime.now();

        for (Foglalas f : osszesFoglalas) {
            if (f.getLejaratIdopontja() != null && f.getLejaratIdopontja().isBefore(most) && !f.getMunkavallalo().isElerheto()) {

                System.out.println(">> AUTOMATIZÁCIÓ: " + f.getMunkavallalo().getNev() + " foglalása lejárt! Felszabadítás...");

                Munkavallalo m = f.getMunkavallalo();
                m.setElerheto(true);
                munkavallaloRepository.save(m);

                foglalasRepository.delete(f);
            }
        }
    }

    public List<Foglalas> osszesFoglalas() {
        return foglalasRepository.findAll();
    }
}