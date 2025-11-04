package br.com.auramed.application.service;

import br.com.auramed.domain.service.PasswordService;
import jakarta.inject.Inject;
import org.springframework.security.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import java.security.SecureRandom;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PasswordServiceImpl implements PasswordService {

    private static final int BCRYPT_ROUNDS = 12;

    @Inject
    Logger logger;

    @Override
    public String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    @Override
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            logger.debug("DEBUG - Verificando senha:");
            logger.debug("DEBUG - Senha plain: " + plainPassword);
            logger.debug("DEBUG - Hash original: " + hashedPassword);

            String normalizedHash = normalizeHashVersion(hashedPassword);
            logger.debug("DEBUG - Hash normalizado: " + normalizedHash);

            boolean result = BCrypt.checkpw(plainPassword, normalizedHash);
            logger.debug("DEBUG - Resultado verificação: " + result);

            return result;
        } catch (Exception e) {
            logger.error("DEBUG - Erro na verificação: " + e.getMessage());
            return false;
        }
    }

    private String normalizeHashVersion(String hashedPassword) {
        if (hashedPassword != null && hashedPassword.startsWith("$2b$")) {
            String normalized = "$2a$" + hashedPassword.substring(4);
            logger.debug("DEBUG - Hash normalizado de 2b para 2a: " + normalized);
            return normalized;
        }
        return hashedPassword;
    }

    @Override
    public boolean isHashedPassword(String password) {
        return password != null && password.matches("^\\$2[aby]\\$.+");
    }
}