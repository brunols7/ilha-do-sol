package com.senac.ilha_do_sol.repositories;

import com.senac.ilha_do_sol.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {

    public List<Users> findByCpf(String cpf);

}
