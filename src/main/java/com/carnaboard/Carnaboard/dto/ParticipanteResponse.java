package com.carnaboard.Carnaboard.dto;

public class ParticipanteResponse {
    private Long usuarioId;
    private Long usuarioPartyId;
    private String nome;

    public ParticipanteResponse(Long usuarioId, Long usuarioPartyId, String nome) {
        this.usuarioId = usuarioId;
        this.usuarioPartyId = usuarioPartyId;
        this.nome = nome;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getUsuarioPartyId() {
        return usuarioPartyId;
    }

    public void setUsuarioPartyId(Long usuarioPartyId) {
        this.usuarioPartyId = usuarioPartyId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
