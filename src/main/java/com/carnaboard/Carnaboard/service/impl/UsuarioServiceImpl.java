package com.carnaboard.Carnaboard.service.impl;

import com.carnaboard.Carnaboard.model.Usuario;
import com.carnaboard.Carnaboard.model.UsuarioRepository;
import com.carnaboard.Carnaboard.service.UsuarioService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario criarUsuario(Usuario usuario) {
        if (usuario.getSenha() == null || usuario.getSenha().length() < 8) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}