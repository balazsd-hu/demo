package hu.nye.demo.controller;

import hu.nye.demo.service.FoglalasService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FoglalasController {

    private final FoglalasService foglalasService;

    public FoglalasController(FoglalasService foglalasService) {
        this.foglalasService = foglalasService;
    }

    @PostMapping("/lefoglal")
    public String lefoglal(@RequestParam Long munkavallaloId) {
        // Mivel még nincs Spring Security loginunk, fixen a "balazs" nevű tesztfelhasználóval (ID: 2) foglalunk
        Long fixFelhasznaloId = 2L;

        try {
            foglalasService.lefoglal(fixFelhasznaloId, munkavallaloId);
        } catch (Exception e) {
            // Ha valami hiba van (pl. már foglalt), elkapjuk, de most simán visszairányítunk
            System.out.println("Hiba a foglalás során: " + e.getMessage());
        }

        // A sikeres foglalás után visszadobjuk a böngészőt a főoldalra, ahol már frissül a státusz!
        return "redirect:/";
    }
}