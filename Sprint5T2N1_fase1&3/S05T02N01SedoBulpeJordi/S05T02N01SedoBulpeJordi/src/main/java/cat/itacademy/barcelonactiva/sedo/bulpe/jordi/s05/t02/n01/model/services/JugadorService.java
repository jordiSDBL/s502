package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.services;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugador;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.dto.JugadorDTO;

import java.util.List;

/**
 * Interfície amb els mètodes de Jugador
 */
public interface JugadorService {
    public void crear(Jugador jugador);
    public List<JugadorDTO> mostrarJugadors();
    public Integer rankingMig();
    public List<JugadorDTO> endrecarRankings();
    public JugadorDTO millorJugador(List<JugadorDTO> llista);
    public JugadorDTO pitjorJugador(List<JugadorDTO> llista);
    public boolean existeixNom(String nom);
    boolean existeixId(Integer id);
    public JugadorDTO convertirEntitatADTO(Jugador jugador);
    public JugadorDTO trobarPerId(Integer id);
    public List<JugadorDTO> trobarJugadorsAmbPercentatge();

}
