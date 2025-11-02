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

    public InfoTeleconsulta(Integer idPaciente, String cdHabilidadeDigital, String cdCanalLembrete,
                            String inPrecisaCuidador, String inJaFezTele) {
        this.idPaciente = idPaciente;
        this.cdHabilidadeDigital = cdHabilidadeDigital;
        this.cdCanalLembrete = cdCanalLembrete;
        this.inPrecisaCuidador = inPrecisaCuidador;
        this.inJaFezTele = inJaFezTele;
        this.dataCadastro = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();

        validarHabilidadeDigital();
        validarCanalLembrete();
        validarPrecisaCuidador();
        validarJaFezTele();
    }

    public void validarHabilidadeDigital() {
        if (cdHabilidadeDigital == null || cdHabilidadeDigital.isBlank()) {
            throw new ValidacaoDeDominioException("Habilidade digital é obrigatória.");
        }

        if (!cdHabilidadeDigital.matches("^(BAIXA|MEDIA|ALTA|NENHUMA)$")) {
            throw new ValidacaoDeDominioException("Habilidade digital deve ser BAIXA, MEDIA, ALTA ou NENHUMA.");
        }
    }

    public void validarCanalLembrete() {
        if (cdCanalLembrete == null || cdCanalLembrete.isBlank()) {
            throw new ValidacaoDeDominioException("Canal de lembrete é obrigatório.");
        }

        if (!cdCanalLembrete.matches("^(WHATSAPP|SMS|EMAIL|TELEFONE)$")) {
            throw new ValidacaoDeDominioException("Canal de lembrete deve ser WHATSAPP, SMS, EMAIL ou TELEFONE.");
        }
    }

    public void validarPrecisaCuidador() {
        if (inPrecisaCuidador == null || inPrecisaCuidador.isBlank()) {
            throw new ValidacaoDeDominioException("Indicador de precisa de cuidador é obrigatório.");
        }

        if (!inPrecisaCuidador.matches("^[SN]$")) {
            throw new ValidacaoDeDominioException("Indicador de precisa de cuidador deve ser S ou N.");
        }
    }

    public void validarJaFezTele() {
        if (inJaFezTele == null || inJaFezTele.isBlank()) {
            throw new ValidacaoDeDominioException("Indicador de já fez teleconsulta é obrigatório.");
        }

        if (!inJaFezTele.matches("^[SN]$")) {
            throw new ValidacaoDeDominioException("Indicador de já fez teleconsulta deve ser S ou N.");
        }
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
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getCdCanalLembrete() {
        return cdCanalLembrete;
    }

    public void setCdCanalLembrete(String cdCanalLembrete) {
        this.cdCanalLembrete = cdCanalLembrete;
        validarCanalLembrete();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getInPrecisaCuidador() {
        return inPrecisaCuidador;
    }

    public void setInPrecisaCuidador(String inPrecisaCuidador) {
        this.inPrecisaCuidador = inPrecisaCuidador;
        validarPrecisaCuidador();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getInJaFezTele() {
        return inJaFezTele;
    }

    public void setInJaFezTele(String inJaFezTele) {
        this.inJaFezTele = inJaFezTele;
        validarJaFezTele();
        this.dataAtualizacao = LocalDateTime.now();
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