package com.carnaboard.Carnaboard.service.impl;

import com.carnaboard.Carnaboard.model.Usuario;
import com.carnaboard.Carnaboard.model.UsuarioRepository;
import com.carnaboard.Carnaboard.service.UsuarioService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario criarUsuario(Usuario usuario) {
        if (usuario.getLogin() == null || usuario.getLogin().isBlank()) {
            throw new IllegalArgumentException("Login obrigatorio");
        }
        String login = usuario.getLogin().trim();
        if (usuarioRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("Login ja existe");
        }
        if (usuario.getSenha() == null || usuario.getSenha().length() < 8) {
            throw new IllegalArgumentException("A senha deve ter no minimo 8 caracteres");
        }
        usuario.setLogin(login);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }

    @Override
    public boolean validarLogin(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            return false;
        }
        return usuarioRepository.findByLogin(login)
                .map(usuario -> passwordEncoder.matches(senha, usuario.getSenha()))
                .orElse(false);
    }

    @Override
    public List<Usuario> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return List.of();
        }
        String termo = nome.trim();
        List<Usuario> porNome = usuarioRepository.findByNomeContainingIgnoreCase(termo);
        List<Usuario> porLogin = usuarioRepository.findByLoginContainingIgnoreCase(termo);
        if (porLogin.isEmpty()) {
            return porNome;
        }
        if (porNome.isEmpty()) {
            return porLogin;
        }
        List<Usuario> unicos = new java.util.ArrayList<>();
        java.util.Set<Long> vistos = new java.util.HashSet<>();
        for (Usuario u : porNome) {
            if (u.getId() != null && vistos.add(u.getId())) {
                unicos.add(u);
            }
        }
        for (Usuario u : porLogin) {
            if (u.getId() != null && vistos.add(u.getId())) {
                unicos.add(u);
            }
        }
        return unicos;
    }

    @Override
    public Usuario atualizarAvatar(Long id, String avatar) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
        usuario.setAvatar(avatar);
        return usuarioRepository.save(usuario);
    }
}
