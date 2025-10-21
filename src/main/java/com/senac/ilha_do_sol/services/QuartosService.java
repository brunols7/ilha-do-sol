package com.senac.ilha_do_sol.services;

import com.senac.ilha_do_sol.entities.Quartos;
import com.senac.ilha_do_sol.repositories.QuartosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuartosService {

    @Autowired
    private QuartosRepository quartosRepository;

    public List<Quartos> buscarTodosQuartos() {
        return quartosRepository.findAll();
    }

    public Optional<Quartos> buscarQuartoPorId(Long id) {
        return quartosRepository.findById(id);
    }

    public Quartos buscarQuartoPorNumero(String numeroQuarto) {
        return quartosRepository.findByNumeroQuarto(numeroQuarto);
    }

    public List<Quartos> buscarQuartosPorCapacidade(int capacidadeMinima) {
        return quartosRepository.findByCapacidadeMaxGreaterThanEqual(capacidadeMinima);
    }

    public List<Quartos> buscarQuartosDisponiveis(LocalDateTime dataCheckIn, LocalDateTime dataCheckOut) {
        return quartosRepository.findByQuartosDisponiveis(dataCheckIn, dataCheckOut);
    }

    public List<Quartos> buscarQuartosDisponiveisPorCapacidade(
            LocalDateTime dataCheckIn,
            LocalDateTime dataCheckOut,
            int capacidadeMinima) {

        List<Quartos> quartosDisponiveis = quartosRepository.findByQuartosDisponiveis(dataCheckIn, dataCheckOut);
        return quartosDisponiveis.stream()
                .filter(quarto -> quarto.getCapacidadeMax() >= capacidadeMinima)
                .toList();
    }

    public Quartos criarQuarto(Quartos quarto) {
        if (quartosRepository.findByNumeroQuarto(quarto.getNumeroQuarto()) != null) {
            throw new IllegalArgumentException("Já existe um quarto com este número");
        }
        return quartosRepository.save(quarto);
    }

    public Quartos atualizarQuarto(Long id, Quartos quartoAtualizado) {
        Optional<Quartos> quartoExistente = quartosRepository.findById(id);

        if (quartoExistente.isEmpty()) {
            throw new IllegalArgumentException("Quarto não encontrado");
        }

        Quartos quarto = quartoExistente.get();

        if (quartoAtualizado.getNome() != null) {
            quarto.setNome(quartoAtualizado.getNome());
        }
        if (quartoAtualizado.getDescricao() != null) {
            quarto.setDescricao(quartoAtualizado.getDescricao());
        }
        if (quartoAtualizado.getCapacidadeMax() > 0) {
            quarto.setCapacidadeMax(quartoAtualizado.getCapacidadeMax());
        }
        if (quartoAtualizado.getCamas() > 0) {
            quarto.setCamas(quartoAtualizado.getCamas());
        }
        if (quartoAtualizado.getValor() > 0) {
            quarto.setValor(quartoAtualizado.getValor());
        }
        if (quartoAtualizado.getImageUrl() != null) {
            quarto.setImageUrl(quartoAtualizado.getImageUrl());
        }

        return quartosRepository.save(quarto);
    }

    public void deletarQuarto(Long id) {
        if (!quartosRepository.existsById(id)) {
            throw new IllegalArgumentException("Quarto não encontrado");
        }
        quartosRepository.deleteById(id);
    }
}
