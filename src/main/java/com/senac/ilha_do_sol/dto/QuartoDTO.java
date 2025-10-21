package com.senac.ilha_do_sol.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuartoDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "Número do quarto é obrigatório")
    private String numeroQuarto;

    @NotNull(message = "Capacidade máxima é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser no mínimo 1")
    private int capacidadeMax;

    @NotNull(message = "Número de camas é obrigatório")
    @Min(value = 1, message = "Deve ter no mínimo 1 cama")
    private int camas;

    @NotNull(message = "Valor é obrigatório")
    @Min(value = 0, message = "Valor deve ser positivo")
    private double valor;

    private String imageUrl;
}
