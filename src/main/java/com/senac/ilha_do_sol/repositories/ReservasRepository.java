package com.senac.ilha_do_sol.repositories;

import com.senac.ilha_do_sol.entities.Quartos;
import com.senac.ilha_do_sol.entities.Reservas;
import com.senac.ilha_do_sol.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservasRepository extends JpaRepository<Reservas, Long> {

    List<Reservas> findByUser(Users user);
    List<Reservas> findByQuarto(Quartos quarto);

}
