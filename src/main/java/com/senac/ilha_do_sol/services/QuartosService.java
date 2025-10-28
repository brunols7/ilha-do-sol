package com.senac.ilha_do_sol.services;

import com.senac.ilha_do_sol.entities.Quartos;
import com.senac.ilha_do_sol.repositories.QuartosRepository;
import com.senac.ilha_do_sol.services.template.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuartosService extends AbstractEntityService<Quartos> {

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
        return criar(quarto);
    }

    public Quartos atualizarQuarto(Long id, Quartos quartoAtualizado) {
        return atualizar(id, quartoAtualizado);
    }

    @Override
    protected void validarCamposObrigatorios(Quartos quarto) {
        if (quarto.getNome() == null || quarto.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do quarto é obrigatório");
        }
        if (quarto.getNumeroQuarto() == null || quarto.getNumeroQuarto().isEmpty()) {
            throw new IllegalArgumentException("Número do quarto é obrigatório");
        }
    }

    @Override
    protected void validarRegrasNegocio(Quartos quarto) {
        if (quarto.getId() == null) {
            Quartos existente = quartosRepository.findByNumeroQuarto(quarto.getNumeroQuarto());
            if (existente != null) {
                throw new IllegalArgumentException("Já existe um quarto com este número");
            }
        }
    }

    @Override
    protected Quartos salvar(Quartos quarto) {
        return quartosRepository.save(quarto);
    }

    @Override
    protected Quartos buscarPorId(Long id) {
        return quartosRepository.findById(id).orElse(null);
    }

    @Override
    protected void aplicarAtualizacoes(Quartos quarto, Quartos quartoAtualizado) {
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
    }

    public void deletarQuarto(Long id) {
        if (!quartosRepository.existsById(id)) {
            throw new IllegalArgumentException("Quarto não encontrado");
        }
        quartosRepository.deleteById(id);
    }
}
