package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.dto;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugada;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class JugadorDTO implements Comparable<JugadorDTO>{
    private Integer id;
    private String nom;
    private LocalDateTime registre;
    private Integer percentatgeMig;

    public void calcularPercentageMig(List<Jugada> jugades){
        int totalVictories = 0;
        if(jugades.size()>0){
            for (int i = 0; i < jugades.size(); i++){
                if(jugades.get(i).isVictoria()){
                    totalVictories += 1;
                }
            }
            this.percentatgeMig = (int) ((Double.valueOf(totalVictories)) / (Double.valueOf(jugades.size())) * 100);
        } else {
            this.percentatgeMig = null;
        }

    }

    @Override
    public int compareTo(JugadorDTO jugadorDTO){
        int estat = -1;
        if(this.percentatgeMig == jugadorDTO.percentatgeMig){
            estat = 0;
        } else if(this.percentatgeMig > jugadorDTO.percentatgeMig){
            estat = 1;
        } return estat;
    }
}
