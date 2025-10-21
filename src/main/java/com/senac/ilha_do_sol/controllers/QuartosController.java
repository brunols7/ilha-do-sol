package com.senac.ilha_do_sol.controllers;

import com.senac.ilha_do_sol.dto.BuscaQuartosDTO;
import com.senac.ilha_do_sol.dto.QuartoDTO;
import com.senac.ilha_do_sol.entities.Quartos;
import com.senac.ilha_do_sol.services.QuartosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quartos")
public class QuartosController {

    @Autowired
    private QuartosService quartosService;

    @GetMapping
    public ResponseEntity<List<Quartos>> listarTodosQuartos() {
        List<Quartos> quartos = quartosService.buscarTodosQuartos();
        return ResponseEntity.ok(quartos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quartos> buscarQuartoPorId(@PathVariable Long id) {
        Optional<Quartos> quarto = quartosService.buscarQuartoPorId(id);
        return quarto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/buscar-disponiveis")
    public ResponseEntity<List<Quartos>> buscarQuartosDisponiveis(@Valid @RequestBody BuscaQuartosDTO buscaDTO) {
        if (buscaDTO.getDataCheckIn() == null || buscaDTO.getDataCheckOut() == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Quartos> quartos;

        if (buscaDTO.getCapacidadeMinima() != null && buscaDTO.getCapacidadeMinima() > 0) {
            quartos = quartosService.buscarQuartosDisponiveisPorCapacidade(
                    buscaDTO.getDataCheckIn(),
                    buscaDTO.getDataCheckOut(),
                    buscaDTO.getCapacidadeMinima()
            );
        } else {
            quartos = quartosService.buscarQuartosDisponiveis(
                    buscaDTO.getDataCheckIn(),
                    buscaDTO.getDataCheckOut()
            );
        }

        return ResponseEntity.ok(quartos);
    }

    @PostMapping
    public ResponseEntity<?> criarQuarto(@Valid @RequestBody QuartoDTO quartoDTO) {
        try {
            Quartos quarto = new Quartos();
            quarto.setNome(quartoDTO.getNome());
            quarto.setDescricao(quartoDTO.getDescricao());
            quarto.setNumeroQuarto(quartoDTO.getNumeroQuarto());
            quarto.setCapacidadeMax(quartoDTO.getCapacidadeMax());
            quarto.setCamas(quartoDTO.getCamas());
            quarto.setValor(quartoDTO.getValor());
            quarto.setImageUrl(quartoDTO.getImageUrl());

            Quartos quartoCriado = quartosService.criarQuarto(quarto);
            return ResponseEntity.status(HttpStatus.CREATED).body(quartoCriado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarQuarto(@PathVariable Long id, @Valid @RequestBody QuartoDTO quartoDTO) {
        try {
            Quartos quartoAtualizado = new Quartos();
            quartoAtualizado.setNome(quartoDTO.getNome());
            quartoAtualizado.setDescricao(quartoDTO.getDescricao());
            quartoAtualizado.setCapacidadeMax(quartoDTO.getCapacidadeMax());
            quartoAtualizado.setCamas(quartoDTO.getCamas());
            quartoAtualizado.setValor(quartoDTO.getValor());
            quartoAtualizado.setImageUrl(quartoDTO.getImageUrl());

            Quartos quarto = quartosService.atualizarQuarto(id, quartoAtualizado);
            return ResponseEntity.ok(quarto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarQuarto(@PathVariable Long id) {
        try {
            quartosService.deletarQuarto(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
