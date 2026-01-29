package com.carnaboard.Carnaboard.service;

import com.carnaboard.Carnaboard.model.Party;

import java.util.List;

public interface PartyService {
    Party criarParty(Party party);
    List<Party> listarParties();
    Party buscarPorId(Long id);
}
