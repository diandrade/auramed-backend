package br.com.auramed.application.service;

import br.com.auramed.domain.service.PasswordService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordServiceImpl implements PasswordService {

    private static final int BCRYPT_ROUNDS = 12;

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
            System.out.println("DEBUG PasswordService - plain: " + plainPassword);
            System.out.println("DEBUG PasswordService - hash: " + hashedPassword);
            boolean result = BCrypt.checkpw(plainPassword, hashedPassword);
            System.out.println("DEBUG PasswordService - result: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("DEBUG PasswordService - error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isHashedPassword(String password) {
        return password != null && password.matches("^\\$2[aby]\\$.+");
    }
}