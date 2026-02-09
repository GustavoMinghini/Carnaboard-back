package com.carnaboard.Carnaboard.controller;

import com.carnaboard.Carnaboard.dto.ParticipantePontuacaoResponse;
import com.carnaboard.Carnaboard.model.Participante;
import com.carnaboard.Carnaboard.service.ParticipanteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/participantes")
public class UsuarioPartyController {

    private final ParticipanteService participanteService;

    public UsuarioPartyController(
            ParticipanteService participanteService
    ) {
        this.participanteService = participanteService;
    }

    @PostMapping
    public ResponseEntity<Participante> adicionarUsuarioNaParty(
            @RequestParam Long usuarioId,
            @RequestParam Long partyId,
            @RequestParam Integer pontos
    ) {
        Participante relacao = participanteService.adicionarUsuarioNaParty(usuarioId, partyId, pontos);
        return ResponseEntity.status(HttpStatus.CREATED).body(relacao);
    }

    @PutMapping("/{id}/pontuacao")
    public ResponseEntity<Participante> atualizarPontuacao(
            @PathVariable Long id,
            @RequestParam Integer pontos
    ) {
        Participante atualizado = participanteService.atualizarPontuacao(id, pontos);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/party/{partyId}/usuarios")
    public ResponseEntity<List<ParticipantePontuacaoResponse>> listarUsuariosDaParty(@PathVariable Long partyId) {
        List<Participante> relacoes = participanteService.listarPorParty(partyId);
        List<ParticipantePontuacaoResponse> response = new ArrayList<>();
        for (Participante relacao : relacoes) {
            if (relacao.getUsuario() == null) {
                continue;
            }
            response.add(new ParticipantePontuacaoResponse(
                    relacao.getUsuario().getId(),
                    relacao.getId(),
                    relacao.getNome(),
                    relacao.getPontuacao()
            ));
        }
        return ResponseEntity.ok(response);
    }
}
