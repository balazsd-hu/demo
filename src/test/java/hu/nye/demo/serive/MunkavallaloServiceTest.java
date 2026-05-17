package hu.nye.demo.service;

import hu.nye.demo.model.Munkavallalo;
import hu.nye.demo.repository.FoglalasRepository;
import hu.nye.demo.repository.MunkavallaloRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Bekapcsoljuk a Mockito kaszkadőr-motort
public class MunkavallaloServiceTest {

    @Mock
    private MunkavallaloRepository munkavallaloRepository; // Kamu adatbázis réteg

    @Mock
    private FoglalasRepository foglalasRepository; // Másik kamu réteg a törléshez

    @InjectMocks
    private MunkavallaloService munkavallaloService; // A valódi Service, amibe a Spring helyett a Mockito pakolja be a kamu repókat

    @Test
    public void testOsszesMunkavallalo() {
        // 1. GIVEN (Adott egy helyzet): Gyártunk két kamu munkást a memóriában
        Munkavallalo m1 = new Munkavallalo(1L, "Teszt Elek", "IT", 5000, "url", true, List.of("Java"));
        Munkavallalo m2 = new Munkavallalo(2L, "Gipsz Jakab", "Barkács", 4000, "url", true, List.of("Fúrás"));

        // Megmondjuk a kamu adatbázisnak, hogyan viselkedjen: "Ha tőled kérik az összes munkást, ezt a listát add vissza!"
        Mockito.when(munkavallaloRepository.findAll()).thenReturn(List.of(m1, m2));

        // 2. WHEN (Amikor történik valami): Meghívjuk a valódi service metódust
        List<Munkavallalo> eredmény = munkavallaloService.osszesMunkavallalo();

        // 3. THEN (Akkor ellenőrizzük az eredményt): A JUnit lecsap
        assertNotNull(eredmény); // Az eredmény nem lehet null
        assertEquals(2, eredmény.size()); // Pontosan 2 embernek kell benne lennie
        assertEquals("Teszt Elek", eredmény.get(0).getNev()); // Az első neve stimmeljen
    }

    @Test
    public void testKeresesIdAlapjan_HaLetezik() {
        // GIVEN
        Munkavallalo m = new Munkavallalo(1L, "Teszt Elek", "IT", 5000, "url", true, List.of("Java"));
        Mockito.when(munkavallaloRepository.findById(1L)).thenReturn(Optional.of(m));

        // WHEN
        Munkavallalo talalat = munkavallaloService.keresesIdAlapjan(1L);

        // THEN
        assertNotNull(talalat);
        assertEquals("Teszt Elek", talalat.getNev());
    }
}