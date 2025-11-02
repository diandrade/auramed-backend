package br.com.auramed.domain.model;

import br.com.auramed.domain.exception.ValidacaoDeDominioException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pessoa {
    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private LocalDate dataNascimento;
    private String genero;
    private String telefone;
    private String tipoPessoa;
    private LocalDateTime dataCadastro;
    private String ativo;

    public Pessoa(String nome, String telefone, String tipoPessoa) {
        this.nome = nome;
        this.telefone = telefone;
        this.tipoPessoa = tipoPessoa;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = "S";
    }

    public void validar() {
        validarNome();
        validarEmail();
        validarCpf();
        validarTelefone();
        validarTipoPessoa();
        validarDataNascimento();
        validarGenero();
    }

    public void validarNome() {
        if (nome == null || nome.isEmpty() || nome.isBlank()) {
            throw new ValidacaoDeDominioException("Nome está vazio.");
        }

        if (nome.length() > 100) {
            throw new ValidacaoDeDominioException("Nome deve ter menos de 100 caracteres.");
        }

        String[] palavras = nome.trim().split("\\s+");
        if (palavras.length < 2) {
            throw new ValidacaoDeDominioException("Nome deve ter pelo menos duas palavras.");
        }

        for (String palavra : palavras) {
            if (palavra.length() < 2) {
                throw new ValidacaoDeDominioException("Cada palavra do nome deve ter pelo menos 2 caracteres.");
            }
        }
    }

    public void validarEmail() {
        if (email == null || email.isBlank()) {
            return;
        }

        final String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(regex)) {
            throw new ValidacaoDeDominioException("Formato de email inválido.");
        }

        if (email.length() > 255) {
            throw new ValidacaoDeDominioException("Email deve ter menos de 255 caracteres.");
        }
    }

    public void validarCpf() {
        if (cpf == null || cpf.isBlank()) {
            return;
        }

        if (!cpf.matches("\\d{11}")) {
            throw new ValidacaoDeDominioException("CPF deve conter exatamente 11 dígitos numéricos.");
        }
    }

    public void validarTelefone() {
        if (telefone == null || telefone.isEmpty() || telefone.isBlank()) {
            throw new ValidacaoDeDominioException("Telefone está vazio.");
        }

        if (!telefone.matches("\\d{10,15}")) {
            throw new ValidacaoDeDominioException("Telefone deve conter entre 10 e 15 dígitos numéricos.");
        }
    }

    public void validarTipoPessoa() {
        if (tipoPessoa == null || tipoPessoa.isEmpty() || tipoPessoa.isBlank()) {
            throw new ValidacaoDeDominioException("Tipo de pessoa está vazio.");
        }

        if (!tipoPessoa.matches("^(CUIDADOR|MEDICO|PACIENTE)$")) {
            throw new ValidacaoDeDominioException("Tipo de pessoa deve ser CUIDADOR, MEDICO ou PACIENTE.");
        }
    }

    public void validarDataNascimento() {
        if (dataNascimento == null) {
            return;
        }

        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new ValidacaoDeDominioException("Data de nascimento não pode ser no futuro.");
        }

        if (dataNascimento.isBefore(LocalDate.now().minusYears(150))) {
            throw new ValidacaoDeDominioException("Data de nascimento inválida.");
        }
    }

    public void validarGenero() {
        if (genero == null || genero.isBlank()) {
            return;
        }

        if (!genero.matches("^[FMO]$")) {
            throw new ValidacaoDeDominioException("Gênero deve ser F (Feminino), M (Masculino) ou O (Outro).");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        validarNome();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        validarEmail();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
        validarCpf();
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        validarDataNascimento();
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
        validarGenero();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
        validarTelefone();
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
        validarTipoPessoa();
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}