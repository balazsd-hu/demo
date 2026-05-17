package hu.nye.demo.repository;

import hu.nye.demo.model.Foglalas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoglalasRepository extends JpaRepository<Foglalas, Long> {
    List<Foglalas> findByFelhasznaloId(Long felhasznaloId);
    void deleteByMunkavallaloId(Long munkavallaloId);
}