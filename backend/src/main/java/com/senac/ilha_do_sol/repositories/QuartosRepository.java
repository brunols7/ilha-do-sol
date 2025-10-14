package com.senac.ilha_do_sol.repositories;

import com.senac.ilha_do_sol.entities.Quartos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface QuartosRepository extends JpaRepository<Quartos, Long> {

    List<Quartos> findByCamas(int camas);
    Quartos findByNumeroQuarto(String numero);
    List<Quartos> findByCapacidadeMaxGreaterThanEqual(int capacidade);

    @Query("SELECT q FROM quartos q WHERE q.id NOT IN (SELECT r.quarto.id FROM reservas r WHERE (r.dataCheckIn <= :dataCheckout AND r.dataCheckOut >= :dataCheckIn))")
    List<Quartos> findByQuartosDisponiveis(LocalDateTime dataCheckIn, LocalDateTime dataCheckOut);
}
