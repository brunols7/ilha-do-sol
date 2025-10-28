package com.senac.ilha_do_sol.services;

import com.senac.ilha_do_sol.entities.Quartos;
import com.senac.ilha_do_sol.entities.Reservas;
import com.senac.ilha_do_sol.entities.Status;
import com.senac.ilha_do_sol.entities.Users;
import com.senac.ilha_do_sol.events.ReservaCanceladaEvent;
import com.senac.ilha_do_sol.events.ReservaCriadaEvent;
import com.senac.ilha_do_sol.events.ReservaStatusAtualizadoEvent;
import com.senac.ilha_do_sol.repositories.QuartosRepository;
import com.senac.ilha_do_sol.repositories.ReservasRepository;
import com.senac.ilha_do_sol.services.strategy.CalculoValorStrategy;
import com.senac.ilha_do_sol.services.strategy.CalculoValorPadraoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservasService {

    @Autowired
    private ReservasRepository reservasRepository;

    @Autowired
    private QuartosRepository quartosRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private CalculoValorStrategy calculoValorStrategy;

    @Autowired
    public ReservasService(ReservasRepository reservasRepository,
                          QuartosRepository quartosRepository,
                          CalculoValorPadraoStrategy calculoValorPadraoStrategy,
                          ApplicationEventPublisher eventPublisher) {
        this.reservasRepository = reservasRepository;
        this.quartosRepository = quartosRepository;
        this.calculoValorStrategy = calculoValorPadraoStrategy;
        this.eventPublisher = eventPublisher;
    }

    public void setCalculoValorStrategy(CalculoValorStrategy calculoValorStrategy) {
        this.calculoValorStrategy = calculoValorStrategy;
    }

    public List<Reservas> buscarTodasReservas() {
        return reservasRepository.findAll();
    }

    public Optional<Reservas> buscarReservaPorId(Long id) {
        return reservasRepository.findById(id);
    }

    public List<Reservas> buscarReservasPorUsuario(Users user) {
        return reservasRepository.findByUser(user);
    }

    public List<Reservas> buscarReservasPorQuarto(Quartos quarto) {
        return reservasRepository.findByQuarto(quarto);
    }

    public Reservas criarReserva(Users user, Long quartoId, LocalDateTime dataCheckIn, LocalDateTime dataCheckOut) {
        if (dataCheckIn.isAfter(dataCheckOut) || dataCheckIn.isEqual(dataCheckOut)) {
            throw new IllegalArgumentException("Data de check-in deve ser anterior à data de check-out");
        }

        if (dataCheckIn.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data de check-in não pode ser no passado");
        }

        Optional<Quartos> quartoOpt = quartosRepository.findById(quartoId);
        if (quartoOpt.isEmpty()) {
            throw new IllegalArgumentException("Quarto não encontrado");
        }

        Quartos quarto = quartoOpt.get();

        List<Quartos> quartosDisponiveis = quartosRepository.findByQuartosDisponiveis(dataCheckIn, dataCheckOut);
        boolean quartoDisponivel = quartosDisponiveis.stream()
                .anyMatch(q -> q.getId().equals(quartoId));

        if (!quartoDisponivel) {
            throw new IllegalArgumentException("Quarto não está disponível nas datas selecionadas");
        }

        Reservas reserva = new Reservas();
        reserva.setUser(user);
        reserva.setQuarto(quarto);
        reserva.setDataCheckIn(dataCheckIn);
        reserva.setDataCheckOut(dataCheckOut);
        reserva.setStatus(Status.CONFIRMADA);
        reserva.setCreatedAt(LocalDateTime.now());

        Reservas reservaSalva = reservasRepository.save(reserva);

        eventPublisher.publishEvent(new ReservaCriadaEvent(this, reservaSalva));

        return reservaSalva;
    }

    public Reservas atualizarStatusReserva(Long reservaId, Status novoStatus) {
        Optional<Reservas> reservaOpt = reservasRepository.findById(reservaId);

        if (reservaOpt.isEmpty()) {
            throw new IllegalArgumentException("Reserva não encontrada");
        }

        Reservas reserva = reservaOpt.get();
        Status statusAnterior = reserva.getStatus();
        reserva.setStatus(novoStatus);

        Reservas reservaAtualizada = reservasRepository.save(reserva);

        eventPublisher.publishEvent(new ReservaStatusAtualizadoEvent(this, reservaAtualizada, statusAnterior, novoStatus));

        return reservaAtualizada;
    }

    public Reservas cancelarReserva(Long reservaId) {
        Reservas reserva = atualizarStatusReserva(reservaId, Status.CANCELADA);
        eventPublisher.publishEvent(new ReservaCanceladaEvent(this, reserva));
        return reserva;
    }

    public void deletarReserva(Long id) {
        if (!reservasRepository.existsById(id)) {
            throw new IllegalArgumentException("Reserva não encontrada");
        }
        reservasRepository.deleteById(id);
    }

    public double calcularValorTotal(Long reservaId) {
        Optional<Reservas> reservaOpt = reservasRepository.findById(reservaId);

        if (reservaOpt.isEmpty()) {
            throw new IllegalArgumentException("Reserva não encontrada");
        }

        Reservas reserva = reservaOpt.get();
        return calculoValorStrategy.calcular(reserva);
    }
}
