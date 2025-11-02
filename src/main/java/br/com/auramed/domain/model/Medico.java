package br.com.auramed.domain.model;

import br.com.auramed.domain.exception.ValidacaoDeDominioException;
import java.time.Instant;

public class Medico {
    private Integer id;
    private Pessoa pessoa;
    private String crm;
    private String aceitaTeleconsulta;
    private Instant dataCadastro;

    public Medico() {
    }

    public Medico(Pessoa pessoa, String crm) {
        this.pessoa = pessoa;
        this.crm = crm;
        this.aceitaTeleconsulta = "S";
        this.dataCadastro = Instant.now();
    }

    public void validarPessoa() {
        if (pessoa == null) {
            throw new ValidacaoDeDominioException("Pessoa é obrigatória para o médico.");
        }
        pessoa.validar();
    }

    public void validarCrm() {
        if (crm == null || crm.trim().isEmpty()) {
            throw new ValidacaoDeDominioException("CRM é obrigatório.");
        }

        String crmLimpo = crm.trim();

        if (crmLimpo.length() > 20) {
            throw new ValidacaoDeDominioException("CRM deve ter no máximo 20 caracteres.");
        }

        if (!crmLimpo.matches("^[A-Z]{2}[\\s-]?\\d+$")) {
            throw new ValidacaoDeDominioException("Formato de CRM inválido. Use formato: UF123456 ou UF-123456");
        }
    }

    public void validarAceitaTeleconsulta() {
        if (aceitaTeleconsulta == null || aceitaTeleconsulta.trim().isEmpty()) {
            throw new ValidacaoDeDominioException("Campo aceita teleconsulta é obrigatório.");
        }

        String status = aceitaTeleconsulta.trim().toUpperCase();
        if (!status.matches("^[SN]$")) {
            throw new ValidacaoDeDominioException("Aceita teleconsulta deve ser S (Sim) ou N (Não).");
        }
    }

    public void validar() {
        validarPessoa();
        validarCrm();
        validarAceitaTeleconsulta();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
        if (pessoa != null) {
            validarPessoa();
        }
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
        if (crm != null) {
            validarCrm();
        }
    }

    public String getAceitaTeleconsulta() {
        return aceitaTeleconsulta;
    }

    public void setAceitaTeleconsulta(String aceitaTeleconsulta) {
        this.aceitaTeleconsulta = aceitaTeleconsulta;
        if (aceitaTeleconsulta != null) {
            validarAceitaTeleconsulta();
        }
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "id=" + id +
                ", pessoa=" + (pessoa != null ? pessoa.getNome() : "null") +
                ", crm='" + crm + '\'' +
                ", aceitaTeleconsulta='" + aceitaTeleconsulta + '\'' +
                ", dataCadastro=" + dataCadastro +
                '}';
    }
}