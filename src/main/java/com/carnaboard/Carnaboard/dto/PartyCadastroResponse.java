package com.carnaboard.Carnaboard.dto;

import java.util.List;

public class PartyCadastroResponse {
    private Long partyId;
    private String nome;
    private List<ParticipanteResponse> participantes;

    public PartyCadastroResponse(Long partyId, String nome, List<ParticipanteResponse> participantes) {
        this.partyId = partyId;
        this.nome = nome;
        this.participantes = participantes;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ParticipanteResponse> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<ParticipanteResponse> participantes) {
        this.participantes = participantes;
    }
}
