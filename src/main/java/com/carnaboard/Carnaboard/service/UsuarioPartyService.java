package com.carnaboard.Carnaboard.service;

import com.carnaboard.Carnaboard.model.Party;
import com.carnaboard.Carnaboard.model.Usuario;
import com.carnaboard.Carnaboard.model.UsuarioParty;

public interface UsuarioPartyService {
    UsuarioParty adicionarUsuarioNaParty(Usuario usuario, Party party, Integer pontos);
    UsuarioParty atualizarPontuacao(Long id, Integer pontos);
}
