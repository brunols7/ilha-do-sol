package com.senac.ilha_do_sol.services.strategy;

import com.senac.ilha_do_sol.entities.Reservas;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class CalculoValorPadraoStrategy implements CalculoValorStrategy {

    @Override
    public double calcular(Reservas reserva) {
        long diasReserva = ChronoUnit.DAYS.between(
            reserva.getDataCheckIn().toLocalDate(),
            reserva.getDataCheckOut().toLocalDate()
        );

        if (diasReserva <= 0) {
            diasReserva = 1;
        }

        return reserva.getQuarto().getValor() * diasReserva;
    }
}
