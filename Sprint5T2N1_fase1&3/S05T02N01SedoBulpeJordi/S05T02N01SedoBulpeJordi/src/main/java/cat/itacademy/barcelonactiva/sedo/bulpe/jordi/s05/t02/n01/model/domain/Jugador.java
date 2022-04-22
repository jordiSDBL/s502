package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Classe d'entitat
 */
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "jugador")
public class Jugador {
    @ApiModelProperty(notes = "Id del jugador - autoincrementat", name = "pk_jugador", required = true)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_jugador;
    @ApiModelProperty(notes = "Nom del jugador", name = "nom", required = true)
    @Column(name="nom") @NotNull
    private String nom;
    @ApiModelProperty(notes = "Data alta de nou jugador", name = "registre", required = true)
    @Column(name="registre") @NotNull
    private LocalDateTime registre;
}


