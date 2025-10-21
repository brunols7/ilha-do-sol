package com.senac.ilha_do_sol.services;

import com.senac.ilha_do_sol.entities.HotelInfo;
import com.senac.ilha_do_sol.repositories.HotelInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelInfoService {

    @Autowired
    private HotelInfoRepository hotelInfoRepository;

    public List<HotelInfo> buscarTodasInformacoes() {
        return hotelInfoRepository.findAll();
    }

    public Optional<HotelInfo> buscarInformacaoPorId(Long id) {
        return hotelInfoRepository.findById(id);
    }

    public HotelInfo buscarInformacaoPrincipal() {
        List<HotelInfo> infos = hotelInfoRepository.findAll();
        if (infos.isEmpty()) {
            throw new IllegalStateException("Nenhuma informação do hotel cadastrada");
        }
        return infos.get(0);
    }

    public HotelInfo criarInformacao(HotelInfo hotelInfo) {
        return hotelInfoRepository.save(hotelInfo);
    }

    public HotelInfo atualizarInformacao(Long id, HotelInfo hotelInfoAtualizada) {
        Optional<HotelInfo> infoExistente = hotelInfoRepository.findById(id);

        if (infoExistente.isEmpty()) {
            throw new IllegalArgumentException("Informação do hotel não encontrada");
        }

        HotelInfo info = infoExistente.get();

        if (hotelInfoAtualizada.getNome() != null) {
            info.setNome(hotelInfoAtualizada.getNome());
        }
        if (hotelInfoAtualizada.getTelefone() != null) {
            info.setTelefone(hotelInfoAtualizada.getTelefone());
        }
        if (hotelInfoAtualizada.getEndereco() != null) {
            info.setEndereco(hotelInfoAtualizada.getEndereco());
        }

        return hotelInfoRepository.save(info);
    }

    public void deletarInformacao(Long id) {
        if (!hotelInfoRepository.existsById(id)) {
            throw new IllegalArgumentException("Informação do hotel não encontrada");
        }
        hotelInfoRepository.deleteById(id);
    }
}
