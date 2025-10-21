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

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
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
