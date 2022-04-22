package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Jugada {
    private boolean victoria;
    private Integer dau1;
    private Integer dau2;
}
