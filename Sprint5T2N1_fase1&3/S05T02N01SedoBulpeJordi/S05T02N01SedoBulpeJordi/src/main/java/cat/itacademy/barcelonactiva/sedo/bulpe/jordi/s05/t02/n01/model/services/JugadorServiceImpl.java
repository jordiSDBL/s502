package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.services;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugada;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugador;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.dto.JugadorDTO;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.repository.JugadorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JugadorServiceImpl implements JugadorService{

    @Autowired
    private JugadorRepo jugadorRepo;
    @Autowired
    private JugadaService jugadaService;

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
    public List<JugadorDTO> mostrarJugadors() {
        return jugadorRepo.findAll().stream().map(this::convertirEntitatADTO).collect(Collectors.toList());
    }



    /**
     * retorna el ranking mitjà de tots els jugadors del sistema.  És a dir, el percentatge mitjà d’èxits.
     * @return
     */
    @Override
    public Integer rankingMig() {
        Integer valor;
        Integer total = 0;
        Integer it = 0;
        List<JugadorDTO> llista = trobarJugadorsAmbPercentatge();

        if (llista.isEmpty()){
            return null;
        }
        for (int i = 0; i < llista.size(); i++){
            if(llista.get(i).getPercentatgeMig()!=null){
                total+= llista.get(i).getPercentatgeMig();
                it++;
            }

        }
        valor = (int) (Double.valueOf(total) / Double.valueOf(it));

        return valor;
    }

    /**
     * retorna el jugador amb pitjor percentatge d’èxit
     * @return
     */
    @Override
    public List<JugadorDTO> endrecarRankings() {
        List<JugadorDTO> llista = new ArrayList<>();
        List<JugadorDTO> jugadors = trobarJugadorsAmbPercentatge();

        for(int i = 0; i < jugadors.size(); i++){
            if(jugadors.get(i).getPercentatgeMig()!=null){
                llista.add(jugadors.get(i));
            }
        }

        Collections.sort(llista);

        return llista;
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
        jugadorDTO.setPk_jugador(jugador.getPk_jugador());
        jugadorDTO.setNom(jugador.getNom());
        jugadorDTO.setRegistre(jugador.getRegistre());

        return jugadorDTO;
    }

    @Override
    public JugadorDTO trobarPerId(Integer id) {
        return convertirEntitatADTO(jugadorRepo.findById(id).orElse(null));
    }

    @Override
    public List<JugadorDTO> trobarJugadorsAmbPercentatge() {

        // LLISTAT DE JUGADORS DTO
        List<JugadorDTO> llistat = mostrarJugadors();

        // LLISTAT DE JUGADES DEL SISTEMA
        List<Jugada> jugades = jugadaService.trobarTirades();

        // LLISTAT DE JUGADES D'UN JUGADOR
        List<Jugada> jugadesJugador = new ArrayList<>();

        int it = 0;
        while (it < llistat.size()){
            for(int i = 0; i < jugades.size(); i++){
                if(jugades.get(i).getJugador().getPk_jugador() == llistat.get(it).getPk_jugador()){
                    jugadesJugador.add(jugades.get(i));
                }
            }
            llistat.get(it).calcularPercentageMig(jugadesJugador);
            jugadesJugador.clear();
            it++;
        }


        return llistat;
    }
}
