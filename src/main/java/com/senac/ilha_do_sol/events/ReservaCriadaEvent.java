package com.senac.ilha_do_sol.events;

import com.senac.ilha_do_sol.entities.Reservas;
import org.springframework.context.ApplicationEvent;

public class ReservaCriadaEvent extends ApplicationEvent {

    private final Reservas reserva;

    public ReservaCriadaEvent(Object source, Reservas reserva) {
        super(source);
        this.reserva = reserva;
    }

    public Reservas getReserva() {
        return reserva;
    }
}
