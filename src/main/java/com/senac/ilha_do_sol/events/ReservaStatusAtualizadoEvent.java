package com.senac.ilha_do_sol.events;

import com.senac.ilha_do_sol.entities.Reservas;
import com.senac.ilha_do_sol.entities.Status;
import org.springframework.context.ApplicationEvent;

public class ReservaStatusAtualizadoEvent extends ApplicationEvent {

    private final Reservas reserva;
    private final Status statusAnterior;
    private final Status novoStatus;

    public ReservaStatusAtualizadoEvent(Object source, Reservas reserva, Status statusAnterior, Status novoStatus) {
        super(source);
        this.reserva = reserva;
        this.statusAnterior = statusAnterior;
        this.novoStatus = novoStatus;
    }

    public Reservas getReserva() {
        return reserva;
    }

    public Status getStatusAnterior() {
        return statusAnterior;
    }

    public Status getNovoStatus() {
        return novoStatus;
    }
}
