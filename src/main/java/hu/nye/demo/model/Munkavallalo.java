package hu.nye.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "munkavallalok")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Munkavallalo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nev;
    private String kategoria; // IT, Barkács, Irodai
    private int oradij;
    private String kepUrl;
    private boolean elerheto = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "munkavallalo_tulajdonsagok", joinColumns = @JoinColumn(name = "munkavallalo_id"))
    @Column(name = "tulajdonsag")
    private List<String> tulajdonsagok;
}