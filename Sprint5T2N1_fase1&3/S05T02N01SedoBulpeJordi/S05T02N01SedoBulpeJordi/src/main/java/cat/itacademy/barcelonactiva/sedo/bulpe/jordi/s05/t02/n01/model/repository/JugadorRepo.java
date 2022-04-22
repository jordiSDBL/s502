package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.repository;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadorRepo extends JpaRepository<Jugador, Integer> {
    boolean existsByNom(String nom);
}
