package com.senac.ilha_do_sol.services.strategy;

import com.senac.ilha_do_sol.entities.Reservas;

public interface CalculoValorStrategy {
    double calcular(Reservas reserva);
}
