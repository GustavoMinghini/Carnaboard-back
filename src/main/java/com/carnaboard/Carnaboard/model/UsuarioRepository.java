package com.carnaboard.Carnaboard.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    List<Usuario> findByLoginContainingIgnoreCase(String login);
}
