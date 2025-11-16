package com.senac.ilha_do_sol.dto;

import com.senac.ilha_do_sol.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarStatusDTO {
    private Status status;
}
