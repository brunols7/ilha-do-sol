package com.senac.ilha_do_sol.dto;

import com.senac.ilha_do_sol.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String cpf;
    private String senha;
    private Role role;
}
