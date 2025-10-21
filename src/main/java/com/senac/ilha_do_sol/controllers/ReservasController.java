package com.senac.ilha_do_sol.controllers;

import com.senac.ilha_do_sol.dto.ReservaDTO;
import com.senac.ilha_do_sol.entities.Reservas;
import com.senac.ilha_do_sol.entities.Status;
import com.senac.ilha_do_sol.entities.Users;
import com.senac.ilha_do_sol.repositories.UsersRepository;
import com.senac.ilha_do_sol.services.ReservasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservasController {

    @Autowired
    private ReservasService reservasService;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping
    public ResponseEntity<List<Reservas>> listarTodasReservas() {
        List<Reservas> reservas = reservasService.buscarTodasReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/minhas")
    public ResponseEntity<?> listarMinhasReservas() {
        try {
            Users user = getUsuarioAutenticado();
            List<Reservas> reservas = reservasService.buscarReservasPorUsuario(user);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservas> buscarReservaPorId(@PathVariable Long id) {
        Optional<Reservas> reserva = reservasService.buscarReservaPorId(id);
        return reserva.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criarReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        try {
            Users user = getUsuarioAutenticado();

            Reservas reserva = reservasService.criarReserva(
                    user,
                    reservaDTO.getQuartoId(),
                    reservaDTO.getDataCheckIn(),
                    reservaDTO.getDataCheckOut()
            );

            double valorTotal = reservasService.calcularValorTotal(reserva.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("reserva", reserva);
            response.put("valorTotal", valorTotal);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        try {
            Reservas reserva = reservasService.cancelarReserva(id);
            return ResponseEntity.ok(reserva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam Status status) {
        try {
            Reservas reserva = reservasService.atualizarStatusReserva(id, status);
            return ResponseEntity.ok(reserva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarReserva(@PathVariable Long id) {
        try {
            reservasService.deletarReserva(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/valor-total")
    public ResponseEntity<?> calcularValorTotal(@PathVariable Long id) {
        try {
            double valorTotal = reservasService.calcularValorTotal(id);
            Map<String, Double> response = new HashMap<>();
            response.put("valorTotal", valorTotal);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Users getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não autenticado"));
    }
}
