package com.senac.ilha_do_sol.listeners;

import com.senac.ilha_do_sol.events.ReservaCanceladaEvent;
import com.senac.ilha_do_sol.events.ReservaCriadaEvent;
import com.senac.ilha_do_sol.events.ReservaStatusAtualizadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaListener.class);

    @EventListener
    public void handleReservaCriada(ReservaCriadaEvent event) {
        logger.info("AUDITORIA: Reserva criada - ID: {}, Usuario: {}, Quarto: {}, Check-in: {}, Check-out: {}",
                event.getReserva().getId(),
                event.getReserva().getUser().getUsername(),
                event.getReserva().getQuarto().getNumeroQuarto(),
                event.getReserva().getDataCheckIn(),
                event.getReserva().getDataCheckOut());
    }

    @EventListener
    public void handleReservaCancelada(ReservaCanceladaEvent event) {
        logger.info("AUDITORIA: Reserva cancelada - ID: {}, Usuario: {}, Quarto: {}",
                event.getReserva().getId(),
                event.getReserva().getUser().getUsername(),
                event.getReserva().getQuarto().getNumeroQuarto());
    }

    @EventListener
    public void handleReservaStatusAtualizado(ReservaStatusAtualizadoEvent event) {
        logger.info("AUDITORIA: Status de reserva atualizado - ID: {}, Status anterior: {}, Novo status: {}",
                event.getReserva().getId(),
                event.getStatusAnterior(),
                event.getNovoStatus());
    }
}
