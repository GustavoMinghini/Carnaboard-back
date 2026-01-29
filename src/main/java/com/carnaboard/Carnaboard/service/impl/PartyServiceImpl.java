package com.carnaboard.Carnaboard.service.impl;

import com.carnaboard.Carnaboard.model.Party;
import com.carnaboard.Carnaboard.model.PartyRepository;
import com.carnaboard.Carnaboard.service.PartyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;

    public PartyServiceImpl(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public Party criarParty(Party party) {
        return partyRepository.save(party);
    }

    @Override
    public List<Party> listarParties() {
        return partyRepository.findAll();
    }

    @Override
    public Party buscarPorId(Long id) {
        return partyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Festa não encontrada"));
    }
}
