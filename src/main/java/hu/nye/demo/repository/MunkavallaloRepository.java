package hu.nye.demo.repository;

import hu.nye.demo.model.Munkavallalo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MunkavallaloRepository extends JpaRepository<Munkavallalo, Long> {
    List<Munkavallalo> findByKategoria(String kategoria);
}