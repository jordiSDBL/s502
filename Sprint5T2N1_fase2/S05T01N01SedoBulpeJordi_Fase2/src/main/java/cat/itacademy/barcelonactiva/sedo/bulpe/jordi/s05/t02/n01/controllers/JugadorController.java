package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.controllers;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugada;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugador;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.services.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/players")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    /*******************************************************************************************************************
                                                    PETICIONS JUGADORS
    *******************************************************************************************************************/

    /**
     * Crear un jugador.
     * Validacions:
     * - Si no es passa "nom" al RequestBody, se li atribueix el valor d'"ANÒNIM".
     * - No pot existir un jugador amb el mateix nom, a excepció, d'ANÒNIM.
     * @param jugador
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<?> crearJugador(@RequestBody Jugador jugador){
        if (jugador.getNom()==null||jugador.getNom().isBlank())
            jugador.setNom("ANÒNIM");

        if(jugadorService.existeixId(jugador.getId()))
            return new ResponseEntity<>("Aquest id ja està agafat.", HttpStatus.BAD_REQUEST);

        if(jugadorService.existeixNom(jugador.getNom())&&(!jugador.getNom().equalsIgnoreCase("ANÒNIM")))
            return new ResponseEntity<>("Aquest jugador ja existeix.", HttpStatus.BAD_REQUEST);

        jugador.setRegistre(LocalDateTime.now());
        jugadorService.crear(jugador);
        return new ResponseEntity<>("El jugador " + jugador.getNom() + " s'ha creat correctament.", HttpStatus.OK);
    }

    /**
     * Modifica un jugador.
     * Validacions:
     * - L'id del RequestBody ha d'existir.
     * - No es pot modificar la data de registre de creació del jugador.
     * @param jugador
     * @return
     */
    @PutMapping("/")
    public ResponseEntity<?> modificarJugador(@RequestBody Jugador jugador){
        if(!jugadorService.existeixId(jugador.getId()))
            return new ResponseEntity<>("Aquest jugador no existeix", HttpStatus.BAD_REQUEST);

        if(jugadorService.existeixNom(jugador.getNom())&&(!jugador.getNom().equalsIgnoreCase("ANÒNIM")))
            return new ResponseEntity<>("Aquest nom ja existeix.", HttpStatus.BAD_REQUEST);

        if(jugador.getRegistre()!=null){
            if(jugadorService.trobarPerId(jugador.getId()).getRegistre()!=jugador.getRegistre())
                return new ResponseEntity<>("No es pot modificar la data de registre original",
                        HttpStatus.BAD_REQUEST);
        }

        jugador.setRegistre(jugadorService.trobarPerId(jugador.getId()).getRegistre());
        jugadorService.crear(jugador);
        return new ResponseEntity<>("S'ha modificat el jugador correctament", HttpStatus.OK);
    }

    /**
     * Mostra tots els jugadors i el seu % d'èxit.
     * Validacions:
     * - Si no hi ha jugadors, no retorna la llista, retorna un missatge.
     * Lògica:
     * mostrarJugadors() busca tots els jugadors de la BBDD i els converteix a DTO, és a dir,
     * que tindrem id, nom, registre i percentatge d'èxit de cada un dels jugadors persistits.
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<?>mostrarJugadors(){

        if(jugadorService.mostrarJugadorsDTO().isEmpty())
            return new ResponseEntity<>("No hi ha jugadors.", HttpStatus.OK);

        return new ResponseEntity<>(jugadorService.mostrarJugadorsDTO(), HttpStatus.OK);
    }

    /*******************************************************************************************************************
                                                PETICIONS JUGADES
    *******************************************************************************************************************/

    /**
     * Un jugador fa una tirada.
     * Fa ús del mètode jugadaService.tirarDaus() que:
     * - desenvolupa la lògica d'una tirada,
     * - retorna el resultat per mostrar-lo a la ResponseEntity.
     * Validacions:
     * - Comprova que el jugador existeix.
     * @param id
     * @return
     */
    @PostMapping("/{id}/games")
    public ResponseEntity<?> ferTirada(@PathVariable("id") Integer id){
        String resultat;

        if(!jugadorService.existeixId(id))
            return new ResponseEntity<>("No existeix cap jugador amb aquest id.", HttpStatus.BAD_REQUEST);

        resultat = (jugadorService.tirarDaus(id))?"guanyat":"perdut";

        return new ResponseEntity<>( jugadorService.trobarPerId(id).getNom() +
                " ha fet una tirada i ha " + resultat + ".",
                HttpStatus.OK);
    }

    /**
     * Eliminar tirades d'un jugador.
     * Validacions:
     * - Comprova que el jugador existeix.
     * Lògica:
     * - Troba i esborra les jugades el jugador de les quals tingui l'id del PathVariable.
     * - Mostra el recompte de jugades eliminades.
     * @param id
     * @return
     */
    @DeleteMapping("/{id}/games")
    public ResponseEntity<?> eliminarTiradesJugador(@PathVariable("id") Integer id){
        if(!jugadorService.existeixId(id))
            return new ResponseEntity<>("No existeix cap jugador amb aquest id.", HttpStatus.BAD_REQUEST);

        String nomJugador = jugadorService.trobarPerId(id).getNom();
        Integer recompte = jugadorService.trobarPerId(id).getJugades().size();

        Jugador jugador = jugadorService.trobarPerId(id);
        jugador.getJugades().clear();
        jugadorService.crear(jugador);

        if(recompte==0){
            return new ResponseEntity<>(nomJugador + " no tenia cap jugada registrada.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("S'han esborrat les tirades de " + nomJugador + "." +
                    "\nJugades eliminades: " + recompte + ".", HttpStatus.OK);
        }
    }


    /**
     * Mostra les jugades d'un jugador.
     * Validacions:
     * - Comprova que el jugador existeix.
     * Lògica:
     * - Troba les jugades el jugador de les quals tingui l'id del PathVariable i les desa una llista.
     * - Mostra el recompte de jugades eliminades.
     * @param id
     * @return
     */
    @GetMapping("/{id}/games")
    public ResponseEntity<?> mostraJugadesJugador(@PathVariable("id") Integer id){
        if(!jugadorService.existeixId(id))
            return new ResponseEntity<>("No existeix cap jugador amb aquest id.", HttpStatus.BAD_REQUEST);

        List<Jugada> jugadesDelJugador = jugadorService.trobarPerId(id).getJugades();
        Integer recompte = jugadesDelJugador.size();

        if(recompte==0){
            return new ResponseEntity<>("Aquest jugador no ha fet cap jugada.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(jugadesDelJugador, HttpStatus.OK);
        }

    }

    /*******************************************************************************************************************
                                                     PETICIONS RANKINGS
    *******************************************************************************************************************/

    /**
     * Calcular percentatge mitjà d'èxit de tots els jugadors.
     * Validacions:
     * - Si és null és que no hi havia jugades.
     * Lògica:
     * - El mètode jugadorService.rankingMig() retorna el càlcul en decimal.
     * @return
     */
    @GetMapping("/ranking")
    public ResponseEntity<?> mostrarRankingsGlobals(){
        Integer valor = jugadorService.rankingMig();

        if(valor==null)
            return new ResponseEntity<>("No hi ha tirades registrades", HttpStatus.OK);

        return new ResponseEntity<>("El percentatge mig d'èxits de tots els jugadors és: " + valor + " %", HttpStatus.OK);
    }

    /**
     * Mostra el pitjor jugador.
     * Validacions:
     * - Que hi hagi tirades per poder fer el càlcul.
     * Lògica:
     * - El mètode pitjorJugador() selecciona l'últim element de la llista.
     *   Prèviament la llista ha estat endreçada amb endrecarRankings() i fent servir Comparable.
     * @return
     */
    @GetMapping("/ranking/loser")
    public ResponseEntity<?> mostrarPitjorJugador(){

        if(jugadorService.endrecarRankings()==null)
            return new ResponseEntity<>("No n'hi ha perquè no hi ha tirades registrades.", HttpStatus.OK);

        return new ResponseEntity<>(jugadorService.pitjorJugador(jugadorService.endrecarRankings()), HttpStatus.OK);
    }

    /**
     * Mostra el pitjor jugador.
     * Validacions:
     * - Que hi hagi tirades per poder fer el càlcul.
     * Lògica:
     * - El mètode millorJugador() selecciona el primer element de la llista.
     *   Prèviament la llista ha estat endreçada amb endrecarRankings() i fent servir Comparable.
     * @return
     */
    @GetMapping("/ranking/winner")
    public ResponseEntity<?> mostrarMillorJugador(){

        if(jugadorService.endrecarRankings()==null)
            return new ResponseEntity<>("No n'hi ha perquè no hi ha tirades registrades.", HttpStatus.OK);

        return new ResponseEntity<>(jugadorService.millorJugador(jugadorService.endrecarRankings()), HttpStatus.OK);
    }
}
