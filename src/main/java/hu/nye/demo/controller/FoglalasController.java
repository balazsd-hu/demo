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
        Long fixFelhasznaloId = 2L;

        try {
            foglalasService.lefoglal(fixFelhasznaloId, munkavallaloId);
        } catch (Exception e) {
            System.out.println("Hiba a foglalás során: " + e.getMessage());
        }

        return "redirect:/";
    }
}