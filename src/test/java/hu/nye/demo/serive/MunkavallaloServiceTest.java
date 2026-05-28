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

@ExtendWith(MockitoExtension.class)
public class MunkavallaloServiceTest {

    @Mock
    private MunkavallaloRepository munkavallaloRepository;

    @Mock
    private FoglalasRepository foglalasRepository;

    @InjectMocks
    private MunkavallaloService munkavallaloService;

    @Test
    public void testOsszesMunkavallalo() {
        Munkavallalo m1 = new Munkavallalo(1L, "Teszt Elek", "IT", 5000, "url", true, List.of("Java"));
        Munkavallalo m2 = new Munkavallalo(2L, "Gipsz Jakab", "Barkács", 4000, "url", true, List.of("Fúrás"));


        Mockito.when(munkavallaloRepository.findAll()).thenReturn(List.of(m1, m2));


        List<Munkavallalo> eredmény = munkavallaloService.osszesMunkavallalo();


        assertNotNull(eredmény);
        assertEquals(2, eredmény.size());
        assertEquals("Teszt Elek", eredmény.get(0).getNev());
    }

    @Test
    public void testKeresesIdAlapjan_HaLetezik() {

        Munkavallalo m = new Munkavallalo(1L, "Teszt Elek", "IT", 5000, "url", true, List.of("Java"));
        Mockito.when(munkavallaloRepository.findById(1L)).thenReturn(Optional.of(m));


        Munkavallalo talalat = munkavallaloService.keresesIdAlapjan(1L);


        assertNotNull(talalat);
        assertEquals("Teszt Elek", talalat.getNev());
    }
}