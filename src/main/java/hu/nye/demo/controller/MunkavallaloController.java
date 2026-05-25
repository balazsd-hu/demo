package hu.nye.demo.controller;

import hu.nye.demo.model.Foglalas;
import hu.nye.demo.service.MunkavallaloService;
import hu.nye.demo.service.FoglalasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MunkavallaloController {

    private final MunkavallaloService munkavallaloService;
    private final FoglalasService foglalasService; // ÚJ BEKÖTÉS

    public MunkavallaloController(MunkavallaloService munkavallaloService, FoglalasService foglalasService) {
        this.munkavallaloService = munkavallaloService;
        this.foglalasService = foglalasService;
    }

    @GetMapping("/")
    public String fooldal(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        model.addAttribute("munkasok", munkavallaloService.osszesMunkavallalo());

        Map<Long, Long> hatralevoIdok = new HashMap<>();
        List<Foglalas> foglalasok = foglalasService.osszesFoglalas();
        LocalDateTime most = LocalDateTime.now();

        for (Foglalas f : foglalasok) {
            if (f.getLejaratIdopontja() != null && f.getLejaratIdopontja().isAfter(most)) {
                long hatralevoPerc = Duration.between(most, f.getLejaratIdopontja()).toMinutes();
                hatralevoIdok.put(f.getMunkavallalo().getId(), hatralevoPerc + 1);
            }
        }
        model.addAttribute("hatralevoIdok", hatralevoIdok);

        if (loggedInUser != null) {
            model.addAttribute("aktualisNev", loggedInUser.getUsername());
        } else {
            model.addAttribute("aktualisNev", "Vendég");
        }

        return "index";
    }
}