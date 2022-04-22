package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe document
 */
@Data @AllArgsConstructor
@Document(collection = "jugador")
public class Jugador {
    @Id
    private Integer id;
    private String nom;
    private LocalDateTime registre;
    private List<Jugada> jugades;

    public Jugador(){
        this.jugades = new ArrayList<>();
    }

}


