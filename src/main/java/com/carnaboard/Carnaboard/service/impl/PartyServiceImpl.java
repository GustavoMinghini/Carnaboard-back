package com.carnaboard.Carnaboard.service.impl;

import com.carnaboard.Carnaboard.model.Participante;
import com.carnaboard.Carnaboard.model.ParticipanteRepository;
import com.carnaboard.Carnaboard.model.ParticipanteTipo;
import com.carnaboard.Carnaboard.model.Party;
import com.carnaboard.Carnaboard.model.PartyRepository;
import com.carnaboard.Carnaboard.model.PartyStatus;
import com.carnaboard.Carnaboard.service.PartyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final ParticipanteRepository participanteRepository;

    public PartyServiceImpl(
            PartyRepository partyRepository,
            ParticipanteRepository participanteRepository
    ) {
        this.partyRepository = partyRepository;
        this.participanteRepository = participanteRepository;
    }

    @Override
    public Party criarParty(Party party) {
        if (party.getStatus() == null) {
            party.setStatus(PartyStatus.ANDAMENTO);
        }
        return partyRepository.save(party);
    }

    @Override
    public List<Party> listarParties() {
        return partyRepository.findAll();
    }

    @Override
    public Party buscarPorId(Long id) {
        return partyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Festa nao encontrada"));
    }

    @Override
    @Transactional
    public Party criarPartyComParticipantes(String nome, List<String> participantes, Long usuarioId) {
        Party party = new Party();
        party.setNome(nome);
        party.setStatus(PartyStatus.ANDAMENTO);
        if (usuarioId != null) {
            party.setCriadorId(usuarioId);
        }
        Party novaParty = partyRepository.save(party);

        if (participantes == null || participantes.isEmpty()) {
            return novaParty;
        }

        List<Participante> relacoes = new ArrayList<>();
        for (String participanteNome : participantes) {
            if (participanteNome == null || participanteNome.isBlank()) {
                continue;
            }
            Participante participante = new Participante();
            participante.setParty(novaParty);
            participante.setNome(participanteNome.trim());
            participante.setTipo(ParticipanteTipo.CONVIDADO);
            participante.setPontuacao(0);
            relacoes.add(participante);
        }

        if (!relacoes.isEmpty()) {
            participanteRepository.saveAll(relacoes);
        }

        return novaParty;
    }

    @Override
    public Party atualizarParty(Long id, String nome) {
        Party party = buscarPorId(id);
        if (nome != null && !nome.isBlank()) {
            party.setNome(nome.trim());
        }
        return partyRepository.save(party);
    }

    @Override
    @Transactional
    public void deletarParty(Long id) {
        participanteRepository.deleteByPartyId(id);
        partyRepository.deleteById(id);
    }

    @Override
    public Party atualizarStatus(Long id, PartyStatus status) {
        Party party = buscarPorId(id);
        party.setStatus(status);
        return partyRepository.save(party);
    }

    @Override
    @Transactional
    public Participante adicionarParticipante(Long partyId, String nome, Integer pontos) {
        Party party = buscarPorId(partyId);
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do participante e obrigatorio");
        }
        Participante participante = new Participante();
        participante.setParty(party);
        participante.setNome(nome.trim());
        participante.setTipo(ParticipanteTipo.CONVIDADO);
        participante.setPontuacao(pontos != null ? pontos : 0);
        return participanteRepository.save(participante);
    }
}
