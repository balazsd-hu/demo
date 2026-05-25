package hu.nye.demo.controller;

import hu.nye.demo.model.Foglalas;
import hu.nye.demo.model.Munkavallalo;
import hu.nye.demo.service.MunkavallaloService;
import hu.nye.demo.service.FoglalasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MunkavallaloService munkavallaloService;
    private final FoglalasService foglalasService;

    public AdminController(MunkavallaloService munkavallaloService, FoglalasService foglalasService) {
        this.munkavallaloService = munkavallaloService;
        this.foglalasService = foglalasService;
    }

    @GetMapping
    public String adminFőoldal(Model model) {
        model.addAttribute("munkasok", munkavallaloService.osszesMunkavallalo());

        Map<Long, Long> hatralevoIdok = new HashMap<>();
        List<Foglalas> foglalasok = foglalasService.osszesFoglalas();
        LocalDateTime most = LocalDateTime.now();

        for (Foglalas f : foglalasok) {
            if (f.getLejaratIdopontja() != null && f.getLejaratIdopontja().isAfter(most)) {
                // Kiszámoljuk a különbséget percekben
                long hatralevoPerc = Duration.between(most, f.getLejaratIdopontja()).toMinutes();
                // Eltároljuk a dolgozó ID-ja alapján a hátralévő időt (+1 perc korrekció a kerekítés miatt)
                hatralevoIdok.put(f.getMunkavallalo().getId(), hatralevoPerc + 1);
            }
        }
        model.addAttribute("hatralevoIdok", hatralevoIdok);

        return "admin";
    }

    @PostMapping("/elerheto/{id}")
    public String elerhetoovaTesz(@PathVariable Long id) {
        Munkavallalo m = munkavallaloService.keresesIdAlapjan(id);
        m.setElerheto(true);
        munkavallaloService.mentes(m);
        return "redirect:/admin";
    }

    @PostMapping("/foglalt/{id}")
    public String foglalttaTesz(@PathVariable Long id) {
        Munkavallalo m = munkavallaloService.keresesIdAlapjan(id);
        m.setElerheto(false);
        munkavallaloService.mentes(m);
        return "redirect:/admin";
    }

    @PostMapping("/hozzaad")
    public String ujMunkas(@RequestParam String nev,
                           @RequestParam String kategoria,
                           @RequestParam int oradij,
                           @RequestParam String kepUrl,
                           @RequestParam String skillekRaw) {

        List<String> tulajdonsagok = new java.util.ArrayList<>(
                Arrays.stream(skillekRaw.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList()
        );

        Munkavallalo uj = new Munkavallalo(null, nev, kategoria, oradij, kepUrl, true, tulajdonsagok);
        munkavallaloService.mentes(uj);

        return "redirect:/admin";
    }

    @GetMapping("/szerkeszt/{id}")
    public String szerkesztesOldal(@PathVariable Long id, Model model) {
        Munkavallalo m = munkavallaloService.keresesIdAlapjan(id);
        model.addAttribute("munkas", m);
        String skillekRaw = String.join(", ", m.getTulajdonsagok());
        model.addAttribute("skillekRaw", skillekRaw);
        return "szerkeszt";
    }

    @PostMapping("/szerkeszt/{id}")
    public String modositasMentese(@PathVariable Long id,
                                   @RequestParam String nev,
                                   @RequestParam String kategoria,
                                   @RequestParam int oradij,
                                   @RequestParam String kepUrl,
                                   @RequestParam String skillekRaw) {

        Munkavallalo m = munkavallaloService.keresesIdAlapjan(id);
        m.setNev(nev);
        m.setKategoria(kategoria);
        m.setOradij(oradij);
        m.setKepUrl(kepUrl);

        List<String> frissTulajdonsagok = new java.util.ArrayList<>(
                Arrays.stream(skillekRaw.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList()
        );
        m.setTulajdonsagok(frissTulajdonsagok);
        munkavallaloService.mentes(m);

        return "redirect:/admin";
    }

    @PostMapping("/torol/{id}")
    public String torles(@PathVariable Long id) {
        munkavallaloService.torles(id);
        return "redirect:/admin";
    }
}