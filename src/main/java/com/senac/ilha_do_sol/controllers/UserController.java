package com.senac.ilha_do_sol.controllers;

import com.senac.ilha_do_sol.entities.Users;
import com.senac.ilha_do_sol.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/perfil")
    public ResponseEntity<?> obterPerfil() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Users user = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Map<String, Object> perfil = new HashMap<>();
            perfil.put("id", user.getId());
            perfil.put("username", user.getUsername());
            perfil.put("email", user.getEmail());
            perfil.put("cpf", user.getCpf());
            perfil.put("role", user.getRole());
            perfil.put("createdAt", user.getCreatedAt());

            return ResponseEntity.ok(perfil);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuário não autenticado");
            return ResponseEntity.status(401).body(error);
        }
    }
}
