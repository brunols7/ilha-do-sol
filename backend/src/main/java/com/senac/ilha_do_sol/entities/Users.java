package com.senac.ilha_do_sol.entities;


import com.senac.ilha_do_sol.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String cpf;
    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    public Users(UserDTO dto, PasswordEncoder encoder){
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.cpf = dto.getCpf();
        this.senha = encoder.encode(dto.getSenha());
        this.role = dto.getRole();
        this.createdAt = LocalDateTime.now();
    }
}
