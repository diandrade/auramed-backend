package br.com.auramed.application.service;

import br.com.auramed.domain.service.AuthenticationService;
import br.com.auramed.domain.service.PasswordService;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Medico;
import br.com.auramed.domain.model.AuthMedico;
import br.com.auramed.domain.repository.AuthMedicoRepository;
import br.com.auramed.domain.repository.MedicoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import org.jboss.logging.Logger;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

@ApplicationScoped
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    AuthMedicoRepository authMedicoRepository;

    @Inject
    MedicoRepository medicoRepository;

    @Inject
    PasswordService passwordService;

    @Inject
    Logger logger;

    @Context
    HttpHeaders httpHeaders;

    private final ConcurrentHashMap<String, TokenInfo> tokens = new ConcurrentHashMap<>();

    private static class TokenInfo {
        Integer medicoId;
        LocalDateTime expiracao;

        TokenInfo(Integer medicoId, LocalDateTime expiracao) {
            this.medicoId = medicoId;
            this.expiracao = expiracao;
        }
    }

    // ✅ MÉTODO CORRIGIDO: Obter médico logado do token da requisição
    @Override
    public Medico getMedicoLogado() throws EntidadeNaoLocalizadaException {
        try {
            String token = obterTokenDaRequisicaoAtual();

            if (token == null || token.isBlank()) {
                logger.error("❌ Token não encontrado na requisição");
                throw new EntidadeNaoLocalizadaException("Token de autenticação não encontrado");
            }

            logger.debug("🔐 Obtendo médico do token: " + token);
            return obterMedicoPorToken(token);

        } catch (EntidadeNaoLocalizadaException e) {
            throw e;
        } catch (Exception e) {
            logger.error("💥 Erro ao obter médico logado: " + e.getMessage());
            throw new EntidadeNaoLocalizadaException("Erro ao obter médico autenticado");
        }
    }

    @Override
    public Integer getMedicoLogadoId() throws EntidadeNaoLocalizadaException {
        return getMedicoLogado().getId();
    }

    // ✅ MÉTODO AUXILIAR: Obter token do header Authorization
    private String obterTokenDaRequisicaoAtual() {
        try {
            if (httpHeaders == null) {
                logger.warn("⚠️ HttpHeaders não disponível no contexto");
                return null;
            }

            List<String> authHeaders = httpHeaders.getRequestHeader("Authorization");
            if (authHeaders == null || authHeaders.isEmpty()) {
                logger.debug("📭 Header Authorization não encontrado");
                return null;
            }

            String authHeader = authHeaders.get(0);
            logger.debug("📨 Authorization header: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                logger.debug("✅ Token extraído: " + token);
                return token;
            }

            logger.warn("⚠️ Formato do header Authorization inválido");
            return null;

        } catch (Exception e) {
            logger.error("💥 Erro ao obter token: " + e.getMessage());
            return null;
        }
    }

    // Seus métodos existentes mantidos abaixo...
    @Override
    public String login(String email, String senha) throws EntidadeNaoLocalizadaException {
        System.out.println("=== AUTH SERVICE LOGIN ===");

        AuthMedico authMedico = authMedicoRepository.buscarPorEmail(email);

        if (authMedico == null) {
            throw new EntidadeNaoLocalizadaException("Credenciais inválidas");
        }

        boolean senhaCorreta = org.mindrot.jbcrypt.BCrypt.checkpw(senha, authMedico.getSenhaHash());
        System.out.println("DEBUG: BCrypt direto: " + senhaCorreta);

        if (!senhaCorreta) {
            throw new EntidadeNaoLocalizadaException("Credenciais inválidas");
        }

        authMedico.setTentativasLogin(0);
        authMedico.setUltimoLogin(LocalDateTime.now());
        authMedicoRepository.editar(authMedico);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracao = LocalDateTime.now().plusHours(24);

        tokens.put(token, new TokenInfo(authMedico.getMedico().getId(), expiracao));

        return token;
    }

    @Override
    public void logout(String token) {
        tokens.remove(token);
    }

    @Override
    public boolean validarToken(String token) {
        TokenInfo tokenInfo = tokens.get(token);
        if (tokenInfo == null) {
            return false;
        }

        if (tokenInfo.expiracao.isBefore(LocalDateTime.now())) {
            tokens.remove(token);
            return false;
        }

        return true;
    }

    @Override
    public Medico obterMedicoPorToken(String token) throws EntidadeNaoLocalizadaException {
        System.out.println("=== OBTER MÉDICO POR TOKEN ===");
        System.out.println("DEBUG: Token: " + token);

        if (!validarToken(token)) {
            System.out.println("DEBUG: Token inválido");
            throw new EntidadeNaoLocalizadaException("Token inválido ou expirado");
        }

        TokenInfo tokenInfo = tokens.get(token);
        System.out.println("DEBUG: TokenInfo: " + tokenInfo);
        System.out.println("DEBUG: Médico ID: " + (tokenInfo != null ? tokenInfo.medicoId : "NULL"));

        Medico medico = medicoRepository.buscarPorId(tokenInfo.medicoId);
        System.out.println("DEBUG: Médico encontrado: " + (medico != null));

        return medico;
    }

    @Override
    public void alterarSenha(String email, String senhaAtual, String novaSenha) throws EntidadeNaoLocalizadaException {
        AuthMedico authMedico = authMedicoRepository.buscarPorEmail(email);

        if (authMedico == null) {
            throw new EntidadeNaoLocalizadaException("Usuário não encontrado");
        }

        if (!passwordService.checkPassword(senhaAtual, authMedico.getSenhaHash())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        String novaSenhaHash = passwordService.hashPassword(novaSenha);
        authMedico.setSenhaHash(novaSenhaHash);
        authMedico.setDataAlteracaoSenha(LocalDateTime.now());

        authMedicoRepository.editar(authMedico);
    }

    @Override
    public void solicitarRecuperacaoSenha(String email) throws EntidadeNaoLocalizadaException {
        throw new UnsupportedOperationException("Funcionalidade não implementada");
    }

    @Override
    public void redefinirSenha(String token, String novaSenha) throws EntidadeNaoLocalizadaException {
        throw new UnsupportedOperationException("Funcionalidade não implementada");
    }
}