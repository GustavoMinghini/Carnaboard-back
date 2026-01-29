package com.carnaboard.Carnaboard.controller;

import com.carnaboard.Carnaboard.model.Party;
import com.carnaboard.Carnaboard.service.PartyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parties")
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
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
}
