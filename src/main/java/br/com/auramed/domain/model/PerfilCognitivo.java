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

    public PerfilCognitivo(Integer idPaciente, String inDificuldadeVisao, String inUsaOculos,
                           String inDificuldadeAudicao, String inUsaAparelhoAud, String inDificuldadeCogn) {
        this.idPaciente = idPaciente;
        this.inDificuldadeVisao = inDificuldadeVisao;
        this.inUsaOculos = inUsaOculos;
        this.inDificuldadeAudicao = inDificuldadeAudicao;
        this.inUsaAparelhoAud = inUsaAparelhoAud;
        this.inDificuldadeCogn = inDificuldadeCogn;
        this.dataCadastro = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();

        validarIndicadores();
    }

    public void validarIndicadores() {
        validarIndicador("Dificuldade visão", inDificuldadeVisao);
        validarIndicador("Uso de óculos", inUsaOculos);
        validarIndicador("Dificuldade audição", inDificuldadeAudicao);
        validarIndicador("Uso de aparelho auditivo", inUsaAparelhoAud);
        validarIndicador("Dificuldade cognitiva", inDificuldadeCogn);
    }

    private void validarIndicador(String campo, String valor) {
        if (valor != null && !valor.isBlank() && !valor.matches("^[SN]$")) {
            throw new ValidacaoDeDominioException(campo + " deve ser S ou N.");
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
        validarIndicador("Dificuldade visão", inDificuldadeVisao);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getInUsaOculos() {
        return inUsaOculos;
    }

    public void setInUsaOculos(String inUsaOculos) {
        this.inUsaOculos = inUsaOculos;
        validarIndicador("Uso de óculos", inUsaOculos);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getInDificuldadeAudicao() {
        return inDificuldadeAudicao;
    }

    public void setInDificuldadeAudicao(String inDificuldadeAudicao) {
        this.inDificuldadeAudicao = inDificuldadeAudicao;
        validarIndicador("Dificuldade audição", inDificuldadeAudicao);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getInUsaAparelhoAud() {
        return inUsaAparelhoAud;
    }

    public void setInUsaAparelhoAud(String inUsaAparelhoAud) {
        this.inUsaAparelhoAud = inUsaAparelhoAud;
        validarIndicador("Uso de aparelho auditivo", inUsaAparelhoAud);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getInDificuldadeCogn() {
        return inDificuldadeCogn;
    }

    public void setInDificuldadeCogn(String inDificuldadeCogn) {
        this.inDificuldadeCogn = inDificuldadeCogn;
        validarIndicador("Dificuldade cognitiva", inDificuldadeCogn);
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