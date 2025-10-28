package com.senac.ilha_do_sol.listeners;

import com.senac.ilha_do_sol.events.ReservaCanceladaEvent;
import com.senac.ilha_do_sol.events.ReservaCriadaEvent;
import com.senac.ilha_do_sol.events.ReservaStatusAtualizadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificacaoListener.class);

    @EventListener
    @Async
    public void handleReservaCriada(ReservaCriadaEvent event) {
        logger.info("NOTIFICACAO: Enviando email de confirmação para {} - Reserva ID: {}",
                event.getReserva().getUser().getEmail(),
                event.getReserva().getId());
    }

    @EventListener
    @Async
    public void handleReservaCancelada(ReservaCanceladaEvent event) {
        logger.info("NOTIFICACAO: Enviando email de cancelamento para {} - Reserva ID: {}",
                event.getReserva().getUser().getEmail(),
                event.getReserva().getId());
    }

    @EventListener
    @Async
    public void handleReservaStatusAtualizado(ReservaStatusAtualizadoEvent event) {
        logger.info("NOTIFICACAO: Enviando email de atualização de status para {} - Reserva ID: {} - Status: {}",
                event.getReserva().getUser().getEmail(),
                event.getReserva().getId(),
                event.getNovoStatus());
    }
}
