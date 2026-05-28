package hu.nye.demo.controller;

import hu.nye.demo.service.FoglalasService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hu.nye.demo.model.Felhasznalo;
import hu.nye.demo.repository.FelhasznaloRepository;

@Controller
public class FoglalasController {

    private final FoglalasService foglalasService;
    private final FelhasznaloRepository felhasznaloRepository;


    public FoglalasController(FoglalasService foglalasService, FelhasznaloRepository felhasznaloRepository) {
        this.foglalasService = foglalasService;
        this.felhasznaloRepository = felhasznaloRepository;
    }

    @PostMapping("/lefoglal")
    public String lefoglal(@RequestParam Long munkavallaloId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        try {

            if (userDetails == null) {
                return "redirect:/login";
            }


            String aktualisUsername = userDetails.getUsername();


            Felhasznalo felhasznalo = felhasznaloRepository.findByUsername(aktualisUsername)
                    .orElseThrow(() -> new RuntimeException("Felhasználó nem található: " + aktualisUsername));


            foglalasService.lefoglal(felhasznalo.getId(), munkavallaloId);

        } catch (Exception e) {
            System.out.println("Hiba a foglalás során: " + e.getMessage());
        }

        return "redirect:/";
    }
}