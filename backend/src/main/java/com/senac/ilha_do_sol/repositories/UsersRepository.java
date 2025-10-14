package com.senac.ilha_do_sol.repositories;

import com.senac.ilha_do_sol.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    List<Users> findByCpf(String cpf);
    Optional<Users> findByEmail(String email);

}
