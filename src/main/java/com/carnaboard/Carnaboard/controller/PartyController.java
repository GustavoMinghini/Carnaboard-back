package com.carnaboard.Carnaboard.controller;

import com.carnaboard.Carnaboard.dto.ParticipanteFestaResponse;
import com.carnaboard.Carnaboard.dto.ParticipanteRequest;
import com.carnaboard.Carnaboard.dto.PartyCadastroRequest;
import com.carnaboard.Carnaboard.dto.PartyCadastroResponse;
import com.carnaboard.Carnaboard.dto.ParticipanteResponse;
import com.carnaboard.Carnaboard.dto.PontuacaoDeltaRequest;
import com.carnaboard.Carnaboard.dto.PartyStatusRequest;
import com.carnaboard.Carnaboard.dto.PartyUpdateRequest;
import com.carnaboard.Carnaboard.model.Participante;
import com.carnaboard.Carnaboard.model.ParticipanteTipo;
import com.carnaboard.Carnaboard.model.Party;
import com.carnaboard.Carnaboard.model.PartyStatus;
import com.carnaboard.Carnaboard.service.PartyService;
import com.carnaboard.Carnaboard.service.ParticipanteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parties")
public class PartyController {

    private final PartyService partyService;
    private final ParticipanteService participanteService;

    public PartyController(
            PartyService partyService,
            ParticipanteService participanteService
    ) {
        this.partyService = partyService;
        this.participanteService = participanteService;
    }

    @PostMapping
    public ResponseEntity<Party> criarParty(@RequestBody Party party) {
        Party novaParty = partyService.criarParty(party);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaParty);
    }

    @GetMapping
    public ResponseEntity<List<Party>> listarParties() {
        return ResponseEntity.ok(partyService.listarParties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Party> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(partyService.buscarPorId(id));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<PartyCadastroResponse> cadastrarParty(@RequestBody PartyCadastroRequest request) {
        Party party = partyService.criarPartyComParticipantes(
                request.getNome(),
                request.getParticipantes(),
                request.getUsuarioId()
        );

        List<ParticipanteResponse> participantes = new ArrayList<>();
        List<Participante> relacoes = participanteService.listarPorParty(party.getId());
        for (Participante relacao : relacoes) {
            participantes.add(new ParticipanteResponse(
                    relacao.getUsuario() != null ? relacao.getUsuario().getId() : null,
                    relacao.getId(),
                    relacao.getNome()
            ));
        }

        PartyCadastroResponse response = new PartyCadastroResponse(
                party.getId(),
                party.getNome(),
                participantes
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/participantes")
    public ResponseEntity<List<ParticipanteFestaResponse>> listarParticipantes(@PathVariable Long id) {
        List<Participante> relacoes = participanteService.listarPorParty(id);
        List<ParticipanteFestaResponse> response = new ArrayList<>();
        for (Participante relacao : relacoes) {
            String tipo = relacao.getTipo() != null ? relacao.getTipo().name() : ParticipanteTipo.CONVIDADO.name();
            String avatar = relacao.getUsuario() != null ? relacao.getUsuario().getAvatar() : null;
            response.add(new ParticipanteFestaResponse(
                    relacao.getId(),
                    relacao.getNome(),
                    tipo,
                    relacao.getUsuario() != null ? relacao.getUsuario().getId() : null,
                    relacao.getPontuacao(),
                    avatar
            ));
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/participantes")
    public ResponseEntity<?> adicionarParticipante(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            @RequestBody ParticipanteRequest request
    ) {
        Party party = partyService.buscarPorId(id);
        if (party.getCriadorId() == null || !party.getCriadorId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Apenas o criador pode editar a festa."));
        }
        Participante relacao = partyService.adicionarParticipante(id, request.getNome(), request.getPontos());
        String tipo = relacao.getTipo() != null ? relacao.getTipo().name() : ParticipanteTipo.CONVIDADO.name();
        String avatar = relacao.getUsuario() != null ? relacao.getUsuario().getAvatar() : null;
        ParticipanteFestaResponse response = new ParticipanteFestaResponse(
                relacao.getId(),
                relacao.getNome(),
                tipo,
                relacao.getUsuario() != null ? relacao.getUsuario().getId() : null,
                relacao.getPontuacao(),
                avatar
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/pontuacoes")
    public ResponseEntity<List<ParticipanteFestaResponse>> aplicarDeltas(
            @PathVariable Long id,
            @RequestBody List<PontuacaoDeltaRequest> deltas
    ) {
        Map<Long, Integer> mapa = new HashMap<>();
        if (deltas != null) {
            for (PontuacaoDeltaRequest item : deltas) {
                if (item == null || item.getParticipanteId() == null || item.getDelta() == null) {
                    continue;
                }
                mapa.put(item.getParticipanteId(), item.getDelta());
            }
        }
        List<Participante> atualizados = participanteService.aplicarDeltas(id, mapa);
        List<ParticipanteFestaResponse> response = new ArrayList<>();
        for (Participante relacao : atualizados) {
            String tipo = relacao.getTipo() != null ? relacao.getTipo().name() : ParticipanteTipo.CONVIDADO.name();
            String avatar = relacao.getUsuario() != null ? relacao.getUsuario().getAvatar() : null;
            response.add(new ParticipanteFestaResponse(
                    relacao.getId(),
                    relacao.getNome(),
                    tipo,
                    relacao.getUsuario() != null ? relacao.getUsuario().getId() : null,
                    relacao.getPontuacao(),
                    avatar
            ));
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/participantes/{participantId}")
    public ResponseEntity<Void> removerParticipante(
            @PathVariable Long id,
            @PathVariable Long participantId
    ) {
        participanteService.removerParticipante(id, participantId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarParty(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            @RequestBody PartyUpdateRequest request
    ) {
        Party party = partyService.buscarPorId(id);
        if (party.getCriadorId() == null || !party.getCriadorId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Apenas o criador pode editar a festa."));
        }
        Party atualizada = partyService.atualizarParty(id, request.getNome());
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarParty(@PathVariable Long id, @RequestParam("userId") Long userId) {
        Party party = partyService.buscarPorId(id);
        if (party.getCriadorId() == null || !party.getCriadorId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Apenas o criador pode excluir a festa."));
        }
        partyService.deletarParty(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Party> atualizarStatus(@PathVariable Long id, @RequestBody PartyStatusRequest request) {
        PartyStatus status = PartyStatus.valueOf(request.getStatus());
        Party atualizada = partyService.atualizarStatus(id, status);
        return ResponseEntity.ok(atualizada);
    }
}
