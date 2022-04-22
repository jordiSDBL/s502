package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.services;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugada;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugador;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.dto.JugadaDTO;

import java.util.List;

public interface JugadaService {
    public boolean tirarDaus(Integer pk_jugador);
    public List<Jugada> trobarTirades();
    public void eliminarJugada(Jugada jugada);
    public JugadaDTO convertirEntitatADTO(Jugada jugada);
    public List<JugadaDTO> convertirLlistaEntitatADTO(List<Jugada> llista);
}
