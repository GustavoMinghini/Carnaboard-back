package com.carnaboard.Carnaboard.controller;

import com.carnaboard.Carnaboard.dto.LoginRequest;
import com.carnaboard.Carnaboard.dto.LoginResponse;
import com.carnaboard.Carnaboard.dto.UsuarioRequest;
import com.carnaboard.Carnaboard.dto.UsuarioResponse;
import com.carnaboard.Carnaboard.dto.AvatarRequest;
import com.carnaboard.Carnaboard.model.Party;
import com.carnaboard.Carnaboard.model.Usuario;
import com.carnaboard.Carnaboard.model.UsuarioRepository;
import com.carnaboard.Carnaboard.service.ParticipanteService;
import com.carnaboard.Carnaboard.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ParticipanteService participanteService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(
            UsuarioService usuarioService,
            ParticipanteService participanteService,
            UsuarioRepository usuarioRepository
    ) {
        this.usuarioService = usuarioService;
        this.participanteService = participanteService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> criarUsuario(@RequestBody UsuarioRequest dto) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setLogin(dto.getLogin());
            usuario.setSenha(dto.getSenha());
            usuario.setAvatar(dto.getAvatar());

            Usuario novoUsuario = usuarioService.criarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<UsuarioResponse> response = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            response.add(new UsuarioResponse(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getLogin(),
                    usuario.getAvatar()
            ));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponse>> buscarPorNome(@RequestParam String nome) {
        List<Usuario> usuarios = usuarioService.buscarPorNome(nome);
        List<UsuarioResponse> response = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            response.add(new UsuarioResponse(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getLogin(),
                    usuario.getAvatar()
            ));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UsuarioResponse>> buscarUsuarios(@RequestParam("q") String nome) {
        List<Usuario> usuarios = usuarioService.buscarPorNome(nome);
        List<UsuarioResponse> response = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            response.add(new UsuarioResponse(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getLogin(),
                    usuario.getAvatar()
            ));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/parties")
    public ResponseEntity<List<Party>> listarPartiesDoUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(participanteService.listarPartiesPorUsuario(id));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        boolean podeLogar = usuarioService.validarLogin(request.getLogin(), request.getSenha());
        UsuarioResponse usuario = null;
        if (podeLogar) {
            usuario = usuarioRepository.findByLogin(request.getLogin())
                    .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getLogin(), u.getAvatar()))
                    .orElse(null);
        }
        return ResponseEntity.ok(new LoginResponse(podeLogar, usuario));
    }

    @PutMapping("/{id}/avatar")
    public ResponseEntity<UsuarioResponse> atualizarAvatar(
            @PathVariable Long id,
            @RequestBody AvatarRequest request
    ) {
        Usuario atualizado = usuarioService.atualizarAvatar(id, request != null ? request.getAvatar() : null);
        UsuarioResponse response = new UsuarioResponse(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getLogin(),
                atualizado.getAvatar()
        );
        return ResponseEntity.ok(response);
    }
}

