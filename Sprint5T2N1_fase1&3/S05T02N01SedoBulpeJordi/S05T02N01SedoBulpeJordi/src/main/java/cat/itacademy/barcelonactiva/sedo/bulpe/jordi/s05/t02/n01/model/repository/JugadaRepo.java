package cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.repository;

import cat.itacademy.barcelonactiva.sedo.bulpe.jordi.s05.t02.n01.model.domain.Jugada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadaRepo extends JpaRepository<Jugada, Integer> {

}
