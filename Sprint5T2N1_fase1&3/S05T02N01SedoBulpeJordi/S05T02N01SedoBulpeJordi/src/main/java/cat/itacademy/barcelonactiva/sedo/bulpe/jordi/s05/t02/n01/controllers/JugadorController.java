package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.controllers;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugada;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugador;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.dto.JugadaDTO;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.services.JugadaService;
import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.services.JugadorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(description = "REST API relacionada amb les entitats Jugador i Jugada.")
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/players")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;
    @Autowired
    private JugadaService jugadaService;

    /*******************************************************************************************************************
                                                    PETICIONS JUGADORS
    *******************************************************************************************************************/

    /**
     * Crear un jugador.
     * Validacions:
     * - Si no es passa "nom" al RequestBody, se li atribueix el valor d'"ANÒNIM".
     * - No pot existir un jugador amb el mateix nom, a excepció, d'ANÒNIM.
     * - Qualsevol usuari validat pot fer aquesta petició.
     * @param jugador
     * @return
     */
    @ApiOperation(value = "Crea un jugador",
            notes = "Dona d'alta a un nou jugador.\n" +
                    "<<Auth.: tots>>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "amb èxit|OK"),
            @ApiResponse(code = 400, message = "ja existeix aquest jugador") })
    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PLAYER')")
    public ResponseEntity<?> crearJugador(@RequestBody Jugador jugador){
        if (jugador.getNom()==null)
            jugador.setNom("ANÒNIM");

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
     * - Petició per usuaris amb permisos d'escriptura.
     * @param jugador
     * @return
     */
    @ApiOperation(value = "Modifica els atributs d'un jugador", notes = "Canvia els atributs, segons el seu id.\n" +
            "<<Auth.:escriptura>>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "amb èxit|OK"),
            @ApiResponse(code = 400, message = "No es pot actualitzar un jugador que no existeix o bé " +
                    "no es pot modificar el registre de creació!!!") })
    @PutMapping("/")
    @PreAuthorize("hasAuthority('player:write')")
    public ResponseEntity<?> modificarJugador(@RequestBody Jugador jugador){
        if(!jugadorService.existeixId(jugador.getPk_jugador()))
            return new ResponseEntity<>("Aquest jugador no existeix", HttpStatus.BAD_REQUEST);
        if(jugador.getRegistre()!=null){
            if(jugadorService.trobarPerId(jugador.getPk_jugador()).getRegistre()!=jugador.getRegistre())
                return new ResponseEntity<>("No es pot modificar la data de registre original",
                        HttpStatus.BAD_REQUEST);
        }
        jugador.setRegistre(jugadorService.trobarPerId(jugador.getPk_jugador()).getRegistre());
        jugadorService.crear(jugador);
        return new ResponseEntity<>("S'ha modificat el jugador correctament", HttpStatus.OK);
    }

    /**
     * Mostra tots els jugadors i el seu % d'èxit.
     * Validacions:
     * - Si no hi ha jugadors, no retorna la llista, retorna un missatge.
     * - Petició per usuaris amb permisos de lectura.
     * @return
     */
    @ApiOperation(value = "Mostra tots el jugadors", notes = "Mostra una llista de jugadors " +
            "amb els seus percentatges d'èxit. DTO.\n" +
            "<<Auth.: lectura>>")
    @ApiResponse(code = 200, message = "amb èxit|OK")
    @GetMapping("/")
    @PreAuthorize("hasAuthority('player:read')")
    public ResponseEntity<?>mostrarJugadors(){
        jugadorService.trobarJugadorsAmbPercentatge();

        if(jugadorService.trobarJugadorsAmbPercentatge().isEmpty())
            return new ResponseEntity<>("No hi ha jugadors.", HttpStatus.OK);

        return new ResponseEntity<>(jugadorService.trobarJugadorsAmbPercentatge(), HttpStatus.OK);
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
     * - Petició només per usuaris amb rol PLAYER
     * @param pk_jugador
     * @return
     */
    @ApiOperation(value = "Fer una tirada de daus",
            notes = "Se li passa l'id d'un jugador i realitza una tirada que s'associarà amb aquest.\n" +
                    "<<Auth.: ROL 'player'>>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "amb èxit|OK"),
            @ApiResponse(code = 400, message = "No existeix el jugador!!!") })
    @PostMapping("/{id}/games")
    @PreAuthorize("hasRole('ROLE_PLAYER')")
    public ResponseEntity<?> ferTirada(@PathVariable("id") Integer pk_jugador){
        String resultat;
        if(!jugadorService.existeixId(pk_jugador))
            return new ResponseEntity<>("No existeix cap jugador amb aquest id.", HttpStatus.BAD_REQUEST);
        resultat = (jugadaService.tirarDaus(pk_jugador))?"guanyat":"perdut";

        return new ResponseEntity<>( jugadorService.trobarPerId(pk_jugador).getNom() +
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
     * - Petició per usuaris amb permisos d'escriptura.
     * @param pk_jugador
     * @return
     */
    @ApiOperation(value = "Esborra les jugades d'un jugador", notes = "Elimina les jugades d'un jugador de la BBDD.\n" +
            "<<Auth.: escriptura>>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "amb èxit|OK"),
            @ApiResponse(code = 400, message = "S'ha passat un id que no existeix!!!") })
    @DeleteMapping("/{id}/games")
    @PreAuthorize("hasAuthority('player:write')")
    public ResponseEntity<?> eliminarTiradesJugador(@PathVariable("id") Integer pk_jugador){
        if(!jugadorService.existeixId(pk_jugador))
            return new ResponseEntity<>("No existeix cap jugador amb aquest id.", HttpStatus.BAD_REQUEST);

        List<Jugada> jugades = jugadaService.trobarTirades();
        Integer recompte = 0;
        String nomJugador = jugadorService.trobarPerId(pk_jugador).getNom();
        for(int i = 0; i < jugades.size(); i++){
            if(jugades.get(i).getJugador().getPk_jugador() == pk_jugador){
                jugadaService.eliminarJugada(jugades.get(i));
                recompte++;
            }
        }
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
     * - Petició per usuaris amb permisos de lectura.
     * @param pk_jugador
     * @return
     */
    @ApiOperation(value = "Mostra les jugades d'un jugador", notes = "Mostra una llista amb totes les jugades " +
            "enregistrades del jugador l'id del qual es passa per paràmetre.\n" +
            "<<Auth.: lectura>>")
    @ApiResponse(code = 200, message = "amb èxit|OK")
    @GetMapping("/{id}/games")
    @PreAuthorize("hasAuthority('player:read')")
    public ResponseEntity<?> mostraJugadesJugador(@PathVariable("id") Integer pk_jugador){
        if(!jugadorService.existeixId(pk_jugador))
            return new ResponseEntity<>("No existeix cap jugador amb aquest id.", HttpStatus.BAD_REQUEST);

        List<Jugada> jugades = jugadaService.trobarTirades();
        List<JugadaDTO> jugadesDelJugador = new ArrayList<>();
        Integer recompte = 0;

        for(int i = 0; i < jugades.size(); i++){
            if(jugades.get(i).getJugador().getPk_jugador() == pk_jugador){
                jugadesDelJugador.add(jugadaService.convertirEntitatADTO(jugades.get(i)));
                recompte++;
            }
        }

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
     * - Petició per usuaris amb permisos de lectura.
     * Lògica:
     * - El mètode jugadorService.rankingMig() retorna el càlcul en decimal.
     * @return
     */
    @ApiOperation(value = "Mostra el ranking global", notes = "Mostra el percentatge d'èxit mig de tots els jugadors.\n" +
            "<<Auth.: lectura>>")
    @ApiResponse(code = 200, message = "amb èxit|OK")
    @GetMapping("/ranking")
    @PreAuthorize("hasAuthority('player:read')")
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
     * - Petició per usuaris amb permisos de lectura.
     * Lògica:
     * - El mètode pitjorJugador() selecciona l'últim element de la llista.
     *   Prèviament la llista ha estat endreçada amb endrecarRankings() i fent servir Comparable.
     * @return
     */
    @ApiOperation(value = "Mostra el pitjor jugador", notes = "Mostra el jugador amb pitjor rendiment.\n" +
            "<<Auth.: lectura>>")
    @ApiResponse(code = 200, message = "amb èxit|OK")
    @GetMapping("/ranking/loser")
    @PreAuthorize("hasAuthority('player:read')")
    public ResponseEntity<?> mostrarPitjorJugador(){

        if(jugadaService.trobarTirades().isEmpty())
            return new ResponseEntity<>("No n'hi ha perquè no hi ha tirades registrades.", HttpStatus.OK);

        return new ResponseEntity<>(jugadorService.pitjorJugador(jugadorService.endrecarRankings()), HttpStatus.OK);
    }

    /**
     * Mostra el millor jugador.
     * Validacions:
     * - Que hi hagi tirades per poder fer el càlcul.
     * - Petició per usuaris amb permisos de lectura.
     * Lògica:
     * - El mètode millorJugador() selecciona el primer element de la llista.
     *   Prèviament la llista ha estat endreçada amb endrecarRankings() i fent servir Comparable.
     * @return
     */
    @ApiOperation(value = "Mostra el millor jugador", notes = "Mostra el jugador amb millor rendiment.\n" +
            "<<Auth.: lectura>>")
    @ApiResponse(code = 200, message = "amb èxit|OK")
    @GetMapping("/ranking/winner")
    @PreAuthorize("hasAuthority('player:read')")
    public ResponseEntity<?> mostrarMillorJugador(){

        if(jugadaService.trobarTirades().isEmpty())
            return new ResponseEntity<>("No n'hi ha perquè no hi ha tirades registrades.", HttpStatus.OK);

        return new ResponseEntity<>(jugadorService.millorJugador(jugadorService.endrecarRankings()), HttpStatus.OK);
    }
}
