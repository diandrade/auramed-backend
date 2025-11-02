package br.com.auramed.domain.service;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Medico;

public interface AuthenticationService {
    String login(String email, String senha) throws EntidadeNaoLocalizadaException;
    void logout(String token);
    boolean validarToken(String token);
    Medico obterMedicoPorToken(String token) throws EntidadeNaoLocalizadaException;
    void alterarSenha(String email, String senhaAtual, String novaSenha) throws EntidadeNaoLocalizadaException;
    void solicitarRecuperacaoSenha(String email) throws EntidadeNaoLocalizadaException;
    void redefinirSenha(String token, String novaSenha) throws EntidadeNaoLocalizadaException;
    Integer getMedicoLogadoId() throws EntidadeNaoLocalizadaException;
    Medico getMedicoLogado() throws EntidadeNaoLocalizadaException;
}