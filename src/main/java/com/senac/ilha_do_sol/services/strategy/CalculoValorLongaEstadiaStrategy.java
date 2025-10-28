package com.senac.ilha_do_sol.services.strategy;

import com.senac.ilha_do_sol.entities.Reservas;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class CalculoValorLongaEstadiaStrategy implements CalculoValorStrategy {

    private static final int DIAS_DESCONTO_MINIMO = 7;
    private static final double PERCENTUAL_DESCONTO = 0.10;

    @Override
    public double calcular(Reservas reserva) {
        long diasReserva = ChronoUnit.DAYS.between(
            reserva.getDataCheckIn().toLocalDate(),
            reserva.getDataCheckOut().toLocalDate()
        );

        if (diasReserva <= 0) {
            diasReserva = 1;
        }

        double valorTotal = reserva.getQuarto().getValor() * diasReserva;

        if (diasReserva >= DIAS_DESCONTO_MINIMO) {
            valorTotal = valorTotal * (1 - PERCENTUAL_DESCONTO);
        }

        return valorTotal;
    }
}
