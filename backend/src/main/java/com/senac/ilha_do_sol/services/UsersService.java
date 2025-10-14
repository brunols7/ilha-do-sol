package com.senac.ilha_do_sol.services;

import com.senac.ilha_do_sol.dto.UserDTO;
import com.senac.ilha_do_sol.entities.Role;
import com.senac.ilha_do_sol.entities.Users;
import com.senac.ilha_do_sol.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getSenha())
                .roles(user.getRole().name())
                .build();
    }

    public Users createUser(UserDTO dto){
        if(usersRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("Email já cadastrado!");
        }

        Users user = new Users(dto, passwordEncoder);
        return usersRepository.save(user);
    }

    public Users login(String email, String senha){
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(!passwordEncoder.matches(senha, user.getSenha())){
            throw new RuntimeException("Senha incorreta");
        }

        return user;
    }

    public Role getRoleUser(Long userId){
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return user.getRole();
    }
}
