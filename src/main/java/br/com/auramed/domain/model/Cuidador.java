package br.com.auramed.domain.model;

import br.com.auramed.domain.exception.ValidacaoDeDominioException;
import java.time.LocalDateTime;

public class Cuidador {
    private Integer idPessoa;
    private String parentesco;
    private String tempoCuidado;
    private LocalDateTime dataCadastro;
    private String ativo;

    public Cuidador(Integer idPessoa) {
        this.idPessoa = idPessoa;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = "S";
    }

    public Cuidador(Integer idPessoa, String parentesco, String tempoCuidado) {
        this.idPessoa = idPessoa;
        this.parentesco = parentesco;
        this.tempoCuidado = tempoCuidado;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = "S";

        validarParentesco();
        validarTempoCuidado();
    }

    public void validarParentesco() {
        if (parentesco == null || parentesco.isBlank()) {
            return;
        }

        if (parentesco.length() > 50) {
            throw new ValidacaoDeDominioException("Parentesco deve ter no máximo 50 caracteres.");
        }
    }

    public void validarTempoCuidado() {
        if (tempoCuidado == null || tempoCuidado.isBlank()) {
            return;
        }

        if (tempoCuidado.length() > 20) {
            throw new ValidacaoDeDominioException("Tempo de cuidado deve ter no máximo 20 caracteres.");
        }
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
        validarParentesco();
    }

    public String getTempoCuidado() {
        return tempoCuidado;
    }

    public void setTempoCuidado(String tempoCuidado) {
        this.tempoCuidado = tempoCuidado;
        validarTempoCuidado();
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
}