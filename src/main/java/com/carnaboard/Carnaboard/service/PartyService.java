package com.carnaboard.Carnaboard.service;

import com.carnaboard.Carnaboard.model.Party;
import com.carnaboard.Carnaboard.model.PartyStatus;
import com.carnaboard.Carnaboard.model.Participante;

import java.util.List;

public interface PartyService {
    Party criarParty(Party party);
    List<Party> listarParties();
    Party buscarPorId(Long id);
    Party criarPartyComParticipantes(String nome, List<String> participantes, Long usuarioId);
    Party atualizarParty(Long id, String nome);
    void deletarParty(Long id);
    Party atualizarStatus(Long id, PartyStatus status);
    Participante adicionarParticipante(Long partyId, String nome, Integer pontos);
}
