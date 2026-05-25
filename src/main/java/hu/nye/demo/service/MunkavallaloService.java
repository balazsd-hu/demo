package hu.nye.demo.service;

import hu.nye.demo.model.Munkavallalo;
import hu.nye.demo.repository.MunkavallaloRepository;
import hu.nye.demo.repository.FoglalasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MunkavallaloService {

    private final MunkavallaloRepository munkavallaloRepository;
    private final FoglalasRepository foglalasRepository;

    public MunkavallaloService(MunkavallaloRepository munkavallaloRepository, FoglalasRepository foglalasRepository) {
        this.munkavallaloRepository = munkavallaloRepository;
        this.foglalasRepository = foglalasRepository;
    }

    public List<Munkavallalo> osszesMunkavallalo() {
        return munkavallaloRepository.findAll();
    }

    public Munkavallalo keresesIdAlapjan(Long id) {
        return munkavallaloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Munkavállaló nem található: " + id));
    }

    public Munkavallalo mentes(Munkavallalo m) {
        return munkavallaloRepository.save(m);
    }

    public List<Munkavallalo> szuresKategoriaAlapjan(String kategoria) {
        return munkavallaloRepository.findByKategoria(kategoria);
    }

    @Transactional
    public void torles(Long id) {

        foglalasRepository.deleteByMunkavallaloId(id);

        munkavallaloRepository.deleteById(id);

    }
}