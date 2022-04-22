package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name="jugada")
public class Jugada {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk_jugada;
    @Column(name = "victoria") @NotNull
    private boolean victoria;
    @Column(name = "dau1")
    private Integer dau1;
    @Column(name = "dau2")
    private Integer dau2;
    @ManyToOne (fetch = FetchType.LAZY) @JoinColumn(name = "jugador_pk_jugador", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Jugador jugador;

    /**
     * Constructor.
     * @param jugador
     */
    public Jugada(Jugador jugador) {
        this.jugador = jugador;
    }
}
