package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.services;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugada;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugador;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.dto.JugadorDTO;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.repository.JugadorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JugadorServiceImpl implements JugadorService{

    @Autowired
    private JugadorRepo jugadorRepo;

    /**
     * Crea un jugador.
     * Segons atributs "registre", s'inicialitzarà a LocalDateTime.now() en aquest moment.
     * @param jugador
     */
    @Override
    public void crear(Jugador jugador) {
        jugadorRepo.save(jugador);
    }




    @Override
    public List<JugadorDTO> mostrarJugadorsDTO() {
        return jugadorRepo.findAll().stream().map(this::convertirEntitatADTO).collect(Collectors.toList());
    }



    /**
     * retorna el ranking mitjà de tots els jugadors del sistema.  És a dir, el percentatge mitjà d’èxits.
     * @return
     */
    @Override
    public Integer rankingMig() {
        Integer valor = 0;
        Integer recompte = 0;
        Integer total = 0;

        List<JugadorDTO> llistaJugadorsDTO = mostrarJugadorsDTO();
        for (int i = 0; i < llistaJugadorsDTO.size(); i++){
            if(llistaJugadorsDTO.get(i).getPercentatgeMig()!=null){
                total += llistaJugadorsDTO.get(i).getPercentatgeMig();
                recompte++;
            }
        }
        if(recompte == 0){
            return null;
        }

        valor = total / recompte;

        return valor;
    }

    /**
     * retorna el jugador amb pitjor percentatge d’èxit
     * @return
     */
    @Override
    public List<JugadorDTO> endrecarRankings() {
        List<JugadorDTO> llistaTotsJugadors = mostrarJugadorsDTO();
        if(llistaTotsJugadors.isEmpty()){
            return null;
        }

        List<JugadorDTO> llistaPercentatges = llistaTotsJugadors.stream().filter(l -> l.getPercentatgeMig() != null).collect(Collectors.toList());

        if(llistaPercentatges.isEmpty()){
            return null;
        } else {
            Collections.sort(llistaPercentatges);
        }

        return llistaPercentatges;
    }

    /**
     * retorna el jugador amb pitjor percentatge d’èxit
     * @return
     */
    @Override
    public JugadorDTO millorJugador(List<JugadorDTO> llista) {
        return llista.get(llista.size()-1);
    }

    @Override
    public JugadorDTO pitjorJugador(List<JugadorDTO> llista) {
        return llista.get(0);
    }

    @Override
    public boolean existeixNom(String nom) {
        return jugadorRepo.existsByNom(nom);
    }

    @Override
    public boolean existeixId(Integer id) {
        return jugadorRepo.existsById(id);
    }

    /**
     * converteix de Jugador a JugadorDTO
     * @param jugador
     * @return
     */
    @Override
    public JugadorDTO convertirEntitatADTO(Jugador jugador) {
        JugadorDTO jugadorDTO = new JugadorDTO();
        jugadorDTO.setId(jugador.getId());
        jugadorDTO.setNom(jugador.getNom());
        jugadorDTO.setRegistre(jugador.getRegistre());
        jugadorDTO.calcularPercentageMig(jugador.getJugades());

        return jugadorDTO;
    }

    @Override
    public Jugador trobarPerId(Integer id) {
        return jugadorRepo.findById(id).orElse(null);
    }

    /**
     * un jugador específic realitza una tirada  dels daus.
     * @param
     */
    @Override
    public boolean tirarDaus(Integer id) {
        int dau1 = generarAleatori();
        int dau2 = generarAleatori();
        int tirada = dau1 + dau2;

        Jugada jugada = new Jugada();
        jugada.setDau1(dau1);
        jugada.setDau2(dau2);

        if (tirada == 7){
            jugada.setVictoria(true);
        } else {
            jugada.setVictoria(false);
        }

        Jugador jugador = trobarPerId(id);
        jugador.getJugades().add(jugada);
        crear(jugador);

        return jugada.isVictoria();
    }

    public int generarAleatori(){
        int superior = 6;
        int numRandom = (int) (Math.random() * superior);

        return numRandom;
    }
}
