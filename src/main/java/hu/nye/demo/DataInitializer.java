package hu.nye.demo;

import hu.nye.demo.model.Felhasznalo;
import hu.nye.demo.model.Munkavallalo;
import hu.nye.demo.repository.FelhasznaloRepository;
import hu.nye.demo.repository.MunkavallaloRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    //private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataInitializer.class);

    private final FelhasznaloRepository felhasznaloRepository;
    private final MunkavallaloRepository munkavallaloRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInitializer(FelhasznaloRepository felhasznaloRepository,
                           MunkavallaloRepository munkavallaloRepository) {
        this.felhasznaloRepository = felhasznaloRepository;
        this.munkavallaloRepository = munkavallaloRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (felhasznaloRepository.count() == 0) {

            String adminJelszoTitkosan = passwordEncoder.encode("admin123");
            String balazsJelszoTitkosan = passwordEncoder.encode("pass123");

            Felhasznalo admin = new Felhasznalo(null, "admin", adminJelszoTitkosan, "ROLE_ADMIN");
            Felhasznalo user = new Felhasznalo(null, "balazs", balazsJelszoTitkosan, "ROLE_USER");

            felhasznaloRepository.save(admin);
            felhasznaloRepository.save(user);
        }

        if (munkavallaloRepository.count() == 0) {

            Munkavallalo m1 = new Munkavallalo(null, "Kovács János", "IT", 8500,
                    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
                    true, List.of("Java", "Spring Boot", "SQL", "Docker"));

            Munkavallalo m2 = new Munkavallalo(null, "Kiss Éva", "IT", 6000,
                    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
                    true, List.of("Excel", "Office", "Adatbevitel"));

            Munkavallalo m3 = new Munkavallalo(null, "Nagy Gábor", "Barkács", 4500,
                    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
                    true, List.of("Bútor összerakás", "Fúrás", "Festés"));

            Munkavallalo m4 = new Munkavallalo(null, "Szabó Anna", "Irodai melók", 5000,
                    "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150",
                    true, List.of("Asszisztens angol+", "Recepciós tapasztalat", "Német B2"));

            munkavallaloRepository.saveAll(List.of(m1, m2, m3, m4));
        }
    }
}