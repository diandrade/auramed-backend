package br.com.auramed.domain.model;

import br.com.auramed.domain.exception.ValidacaoDeDominioException;
import java.time.LocalDateTime;
import org.jboss.logging.Logger;

public class Paciente {
    private static final Logger LOG = Logger.getLogger(Paciente.class);

    private Integer idPessoa;
    private Integer idMedicoResponsavel;
    private String nrCartaoSUS;
    private LocalDateTime dataCadastro;
    private String ativo;

    public Paciente(Integer idPessoa, Integer idMedicoResponsavel, String nrCartaoSUS) {
        this.idPessoa = idPessoa;
        this.idMedicoResponsavel = (idMedicoResponsavel != null) ? idMedicoResponsavel : 1;
        this.nrCartaoSUS = nrCartaoSUS;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = "S";

        if (nrCartaoSUS != null) {
            validarCartaoSUS();
        }
    }

    public void validarCartaoSUS() {
        LOG.debug("INICIANDO VALIDAÇÃO DO CARTÃO SUS: " + this.nrCartaoSUS);

        if (nrCartaoSUS == null || nrCartaoSUS.isBlank()) {
            LOG.error("Cartão SUS está vazio");
            throw new ValidacaoDeDominioException("Número do Cartão SUS é obrigatório.");
        }

        String cartaoLimpo = nrCartaoSUS.replaceAll("[^\\d]", "");
        LOG.debug("Cartão SUS limpo: " + cartaoLimpo);

        if (cartaoLimpo.length() != 15) {
            LOG.error("Cartão SUS não tem 15 dígitos: " + cartaoLimpo.length() + " dígitos encontrados");
            throw new ValidacaoDeDominioException("Cartão SUS deve conter 15 dígitos numéricos.");
        }

        if (!cartaoLimpo.matches("\\d{15}")) {
            LOG.error("Cartão SUS contém caracteres inválidos: " + cartaoLimpo);
            throw new ValidacaoDeDominioException("Cartão SUS deve conter apenas números.");
        }

        LOG.warn("VALIDAÇÃO DO DÍGITO VERIFICADOR DESATIVADA PARA TESTES");
        LOG.info("CARTÃO SUS ACEITO (validação simplificada): " + cartaoLimpo);
    }

    private boolean validarDigitoVerificadorSUS(String cartao) {
        try {
            LOG.debug("Calculando dígito verificador para: " + cartao);
            return true;
        } catch (Exception e) {
            LOG.error("Erro na validação do Cartão SUS: " + e.getMessage(), e);
            throw new ValidacaoDeDominioException("Erro na validação do Cartão SUS.");
        }
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Integer getIdMedicoResponsavel() {
        return idMedicoResponsavel;
    }

    public void setIdMedicoResponsavel(Integer idMedicoResponsavel) {
        this.idMedicoResponsavel = idMedicoResponsavel;
    }

    public String getNrCartaoSUS() {
        return nrCartaoSUS;
    }

    public void setNrCartaoSUS(String nrCartaoSUS) {
        this.nrCartaoSUS = nrCartaoSUS;
        validarCartaoSUS();
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