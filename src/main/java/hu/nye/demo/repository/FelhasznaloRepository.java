package hu.nye.demo.repository;

import hu.nye.demo.model.Felhasznalo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FelhasznaloRepository extends JpaRepository<Felhasznalo, Long> {
    Optional<Felhasznalo> findByUsername(String username);
}