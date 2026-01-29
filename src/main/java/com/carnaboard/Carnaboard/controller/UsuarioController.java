package com.carnaboard.Carnaboard.controller;

import com.carnaboard.Carnaboard.dto.UsuarioRequest;
import com.carnaboard.Carnaboard.model.Usuario;
import com.carnaboard.Carnaboard.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> criarUsuario(@RequestBody UsuarioRequest dto) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setLogin(dto.getLogin());
            usuario.setSenha(dto.getSenha());

            Usuario novoUsuario = usuarioService.criarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}

