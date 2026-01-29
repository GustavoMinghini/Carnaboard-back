package com.carnaboard.Carnaboard.controller;

import com.carnaboard.Carnaboard.model.Party;
import com.carnaboard.Carnaboard.model.Usuario;
import com.carnaboard.Carnaboard.model.UsuarioParty;
import com.carnaboard.Carnaboard.service.PartyService;
import com.carnaboard.Carnaboard.service.UsuarioPartyService;
import com.carnaboard.Carnaboard.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario-party")
public class UsuarioPartyController {

    private final UsuarioPartyService usuarioPartyService;
    private final UsuarioService usuarioService;
    private final PartyService partyService;

    public UsuarioPartyController(
            UsuarioPartyService usuarioPartyService,
            UsuarioService usuarioService,
            PartyService partyService
    ) {
        this.usuarioPartyService = usuarioPartyService;
        this.usuarioService = usuarioService;
        this.partyService = partyService;
    }

    @PostMapping
    public ResponseEntity<UsuarioParty> adicionarUsuarioNaParty(
            @RequestParam Long usuarioId,
            @RequestParam Long partyId,
            @RequestParam Integer pontos
    ) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Party party = partyService.buscarPorId(partyId);
        UsuarioParty relacao = usuarioPartyService.adicionarUsuarioNaParty(usuario, party, pontos);
        return ResponseEntity.status(HttpStatus.CREATED).body(relacao);
    }

    @PutMapping("/{id}/pontuacao")
    public ResponseEntity<UsuarioParty> atualizarPontuacao(
            @PathVariable Long id,
            @RequestParam Integer pontos
    ) {
        UsuarioParty atualizado = usuarioPartyService.atualizarPontuacao(id, pontos);
        return ResponseEntity.ok(atualizado);
    }
}
