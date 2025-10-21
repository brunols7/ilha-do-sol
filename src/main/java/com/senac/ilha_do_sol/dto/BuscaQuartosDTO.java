package com.senac.ilha_do_sol.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuscaQuartosDTO {

    @NotNull(message = "Data de check-in é obrigatória")
    @Future(message = "Data de check-in deve ser no futuro")
    private LocalDateTime dataCheckIn;

    @NotNull(message = "Data de check-out é obrigatória")
    @Future(message = "Data de check-out deve ser no futuro")
    private LocalDateTime dataCheckOut;

    @Min(value = 1, message = "Capacidade mínima deve ser pelo menos 1")
    private Integer capacidadeMinima;
}
