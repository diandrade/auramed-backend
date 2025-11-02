package br.com.auramed.domain.model;

import br.com.auramed.domain.exception.ValidacaoDeDominioException;

public class Especialidade {
    private Integer id;
    private String nome;
    private String descricao;
    private String ativo;

    public Especialidade(String nome) {
        this.nome = nome;
        this.ativo = "S";
    }

    public void validarNome() {
        if (nome == null || nome.isEmpty() || nome.isBlank()) {
            throw new ValidacaoDeDominioException("Nome da especialidade está vazio.");
        }

        if (nome.length() > 100) {
            throw new ValidacaoDeDominioException("Nome da especialidade deve ter menos de 100 caracteres.");
        }

        if (nome.length() < 3) {
            throw new ValidacaoDeDominioException("Nome da especialidade deve ter pelo menos 3 caracteres.");
        }
    }

    public void validarDescricao() {
        if (descricao != null && descricao.length() > 500) {
            throw new ValidacaoDeDominioException("Descrição da especialidade deve ter menos de 500 caracteres.");
        }
    }

    public void validarAtivo() {
        if (ativo == null || ativo.isEmpty() || ativo.isBlank()) {
            throw new ValidacaoDeDominioException("Status da especialidade está vazio.");
        }

        if (!ativo.matches("^[SN]$")) {
            throw new ValidacaoDeDominioException("Status da especialidade deve ser S (Ativa) ou N (Inativa).");
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
        validarDescricao();
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
        validarAtivo();
    }
}