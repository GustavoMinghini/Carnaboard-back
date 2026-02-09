package com.carnaboard.Carnaboard.service;

import com.carnaboard.Carnaboard.model.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario criarUsuario(Usuario usuario);
    List<Usuario> listarUsuarios();
    Usuario buscarPorId(Long id);
    boolean validarLogin(String login, String senha);
    List<Usuario> buscarPorNome(String nome);
    Usuario atualizarAvatar(Long id, String avatar);
}
