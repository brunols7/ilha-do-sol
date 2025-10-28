package com.senac.ilha_do_sol.services.strategy;

import com.senac.ilha_do_sol.entities.Reservas;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

@Component
public class CalculoValorTemporadaStrategy implements CalculoValorStrategy {

    private static final double MULTIPLICADOR_ALTA_TEMPORADA = 1.30;

    @Override
    public double calcular(Reservas reserva) {
        long diasReserva = ChronoUnit.DAYS.between(
            reserva.getDataCheckIn().toLocalDate(),
            reserva.getDataCheckOut().toLocalDate()
        );
        if (diasReserva <= 0) {
            diasReserva = 1;
        }

        double valorBase = reserva.getQuarto().getValor();
        double valorTotal = 0.0;

        LocalDate dataAtual = reserva.getDataCheckIn().toLocalDate();
        LocalDate dataFim = reserva.getDataCheckOut().toLocalDate();

        while (dataAtual.isBefore(dataFim)) {
            if (isAltaTemporada(dataAtual)) {
                valorTotal += valorBase * MULTIPLICADOR_ALTA_TEMPORADA;
            } else {
                valorTotal += valorBase;
            }
            dataAtual = dataAtual.plusDays(1);
        }

        return valorTotal;
    }

    private boolean isAltaTemporada(LocalDate data) {
        Month mes = data.getMonth();
        return mes == Month.DECEMBER || mes == Month.JANUARY || mes == Month.JULY;
    }
}
