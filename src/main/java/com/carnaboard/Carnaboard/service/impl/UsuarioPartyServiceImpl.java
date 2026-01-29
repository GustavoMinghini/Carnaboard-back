package com.carnaboard.Carnaboard.service.impl;

import com.carnaboard.Carnaboard.model.Party;
import com.carnaboard.Carnaboard.model.Usuario;
import com.carnaboard.Carnaboard.model.UsuarioParty;
import com.carnaboard.Carnaboard.model.UsuarioPartyRepository;
import com.carnaboard.Carnaboard.service.UsuarioPartyService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioPartyServiceImpl implements UsuarioPartyService {

    private final UsuarioPartyRepository usuarioPartyRepository;

    public UsuarioPartyServiceImpl(UsuarioPartyRepository usuarioPartyRepository) {
        this.usuarioPartyRepository = usuarioPartyRepository;
    }

    @Override
    public UsuarioParty adicionarUsuarioNaParty(Usuario usuario, Party party, Integer pontos) {
        UsuarioParty usuarioParty = new UsuarioParty();
        usuarioParty.setUsuario(usuario);
        usuarioParty.setParty(party);
        usuarioParty.setPontos(pontos);
        return usuarioPartyRepository.save(usuarioParty);
    }

    @Override
    public UsuarioParty atualizarPontuacao(Long id, Integer pontos) {
        UsuarioParty usuarioParty = usuarioPartyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participação não encontrada"));
        usuarioParty.setPontos(pontos);
        return usuarioPartyRepository.save(usuarioParty);
    }
}
