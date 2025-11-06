package br.com.auramed.domain.model;

import br.com.auramed.domain.exception.ValidacaoDeDominioException;
import java.time.LocalDateTime;

public class PerfilCognitivo {
    private Integer idPerfilCognitivo;
    private Integer idPaciente;
    private String inDificuldadeVisao;
    private String inUsaOculos;
    private String inDificuldadeAudicao;
    private String inUsaAparelhoAud;
    private String inDificuldadeCogn;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public PerfilCognitivo() {}

    public void validarIndicadores() {
        validarCampoSN(inDificuldadeVisao, "Dificuldade visão");
        validarCampoSN(inUsaOculos, "Usa óculos");
        validarCampoSN(inDificuldadeAudicao, "Dificuldade audição");
        validarCampoSN(inUsaAparelhoAud, "Usa aparelho auditivo");
        validarCampoSN(inDificuldadeCogn, "Dificuldade cognitiva");
    }

    private void validarCampoSN(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            return;
        }
        if (!valor.matches("^[SN]$")) {
            throw new ValidacaoDeDominioException(campo + " deve ser S (Sim) ou N (Não).");
        }
    }

    public Integer getIdPerfilCognitivo() {
        return idPerfilCognitivo;
    }

    public void setIdPerfilCognitivo(Integer idPerfilCognitivo) {
        this.idPerfilCognitivo = idPerfilCognitivo;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getInDificuldadeVisao() {
        return inDificuldadeVisao;
    }

    public void setInDificuldadeVisao(String inDificuldadeVisao) {
        this.inDificuldadeVisao = inDificuldadeVisao;
    }

    public String getInUsaOculos() {
        return inUsaOculos;
    }

    public void setInUsaOculos(String inUsaOculos) {
        this.inUsaOculos = inUsaOculos;
    }

    public String getInDificuldadeAudicao() {
        return inDificuldadeAudicao;
    }

    public void setInDificuldadeAudicao(String inDificuldadeAudicao) {
        this.inDificuldadeAudicao = inDificuldadeAudicao;
    }

    public String getInUsaAparelhoAud() {
        return inUsaAparelhoAud;
    }

    public void setInUsaAparelhoAud(String inUsaAparelhoAud) {
        this.inUsaAparelhoAud = inUsaAparelhoAud;
    }

    public String getInDificuldadeCogn() {
        return inDificuldadeCogn;
    }

    public void setInDificuldadeCogn(String inDificuldadeCogn) {
        this.inDificuldadeCogn = inDificuldadeCogn;
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