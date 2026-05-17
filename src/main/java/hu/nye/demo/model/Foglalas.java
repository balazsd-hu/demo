package hu.nye.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;


@Entity
@Table(name = "foglalasok")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Foglalas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "felhasznalo_id", nullable = false)
    private Felhasznalo felhasznalo;

    @ManyToOne
    @JoinColumn(name = "munkavallalo_id", nullable = false)
    private Munkavallalo munkavallalo;

    private LocalDateTime foglalasIdopontja;
    private LocalDateTime lejaratIdopontja;
}