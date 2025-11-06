package br.com.auramed.domain.model;

import br.com.auramed.domain.exception.ValidacaoDeDominioException;
import java.time.LocalDateTime;

public class InfoTeleconsulta {
    private Integer idInfoTeleconsulta;
    private Integer idPaciente;
    private String cdHabilidadeDigital;
    private String cdCanalLembrete;
    private String inPrecisaCuidador;
    private String inJaFezTele;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public InfoTeleconsulta() {}

    public void validarHabilidadeDigital() {
        if (cdHabilidadeDigital == null || cdHabilidadeDigital.isBlank()) {
            return;
        }
        if (!cdHabilidadeDigital.matches("^(BAIXA|MEDIA|ALTA)$")) {
            throw new ValidacaoDeDominioException("Habilidade digital deve ser BAIXA, MEDIA ou ALTA.");
        }
    }

    public void validarCanalLembrete() {
        if (cdCanalLembrete == null || cdCanalLembrete.isBlank()) {
            return;
        }
        if (!cdCanalLembrete.matches("^(WHATSAPP|SMS|EMAIL|TELEFONE)$")) {
            throw new ValidacaoDeDominioException("Canal de lembrete deve ser WHATSAPP, SMS, EMAIL ou TELEFONE.");
        }
    }

    public void validarPrecisaCuidador() {
        if (inPrecisaCuidador == null || inPrecisaCuidador.isBlank()) {
            return;
        }
        if (!inPrecisaCuidador.matches("^[SN]$")) {
            throw new ValidacaoDeDominioException("Precisa cuidador deve ser S (Sim) ou N (Não).");
        }
    }

    public void validarJaFezTele() {
        if (inJaFezTele == null || inJaFezTele.isBlank()) {
            return;
        }
        if (!inJaFezTele.matches("^[SN]$")) {
            throw new ValidacaoDeDominioException("Já fez teleconsulta deve ser S (Sim) ou N (Não).");
        }
    }

    public void validar() {
        validarHabilidadeDigital();
        validarCanalLembrete();
        validarPrecisaCuidador();
        validarJaFezTele();
    }

    public Integer getIdInfoTeleconsulta() {
        return idInfoTeleconsulta;
    }

    public void setIdInfoTeleconsulta(Integer idInfoTeleconsulta) {
        this.idInfoTeleconsulta = idInfoTeleconsulta;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getCdHabilidadeDigital() {
        return cdHabilidadeDigital;
    }

    public void setCdHabilidadeDigital(String cdHabilidadeDigital) {
        this.cdHabilidadeDigital = cdHabilidadeDigital;
        validarHabilidadeDigital();
    }

    public String getCdCanalLembrete() {
        return cdCanalLembrete;
    }

    public void setCdCanalLembrete(String cdCanalLembrete) {
        this.cdCanalLembrete = cdCanalLembrete;
        validarCanalLembrete();
    }

    public String getInPrecisaCuidador() {
        return inPrecisaCuidador;
    }

    public void setInPrecisaCuidador(String inPrecisaCuidador) {
        this.inPrecisaCuidador = inPrecisaCuidador;
        validarPrecisaCuidador();
    }

    public String getInJaFezTele() {
        return inJaFezTele;
    }

    public void setInJaFezTele(String inJaFezTele) {
        this.inJaFezTele = inJaFezTele;
        validarJaFezTele();
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}