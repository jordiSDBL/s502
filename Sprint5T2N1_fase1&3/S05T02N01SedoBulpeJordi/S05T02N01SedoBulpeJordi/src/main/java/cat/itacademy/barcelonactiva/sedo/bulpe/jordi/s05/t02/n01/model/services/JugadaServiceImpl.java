package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.services;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugada;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.dto.JugadaDTO;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.repository.JugadaRepo;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.repository.JugadorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JugadaServiceImpl implements JugadaService{

    @Autowired
    private JugadaRepo jugadaRepo;
    @Autowired
    private JugadorRepo jugadorRepo;

    /**
     * un jugador específic realitza una tirada  dels daus.
     * @param
     */
    @Override
    public boolean tirarDaus(Integer pk_jugador) {
        int dau1 = generarAleatori();
        int dau2 = generarAleatori();
        int tirada = dau1 + dau2;

        Jugada jugada = new Jugada(jugadorRepo.findById(pk_jugador).orElse(null));
        jugada.setDau1(dau1);
        jugada.setDau2(dau2);

        if (tirada == 7){
            jugada.setVictoria(true);
        } else {
            jugada.setVictoria(false);
        }

        jugadaRepo.save(jugada);

        return jugada.isVictoria();
    }


    /**
     * retorna el llistat de jugades per un jugador.
     * @return
     */
    @Override
    public List<Jugada> trobarTirades() {
        return jugadaRepo.findAll();
        }

    @Override
    public void eliminarJugada(Jugada jugada) {
        jugadaRepo.delete(jugada);
    }

    @Override
    public JugadaDTO convertirEntitatADTO(Jugada jugada) {
        JugadaDTO jugadaDTO = new JugadaDTO();
        jugadaDTO.setPk_jugada(jugada.getPk_jugada());
        jugadaDTO.setVictoria(jugada.isVictoria());
        jugadaDTO.setDau1(jugada.getDau1());
        jugadaDTO.setDau2(jugada.getDau2());

        return jugadaDTO;
    }

    @Override
    public List<JugadaDTO> convertirLlistaEntitatADTO(List<Jugada> llista) {
        return llista.stream().map(this::convertirEntitatADTO).collect(Collectors.toList());
    }

    public int generarAleatori(){
        int superior = 6;
        int numRandom = (int) (Math.random() * superior);

        return numRandom;
    }
}
