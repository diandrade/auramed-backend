package br.com.auramed.interfaces.dto.response;

import java.time.LocalDateTime;

public class PerfilCognitivoResponseDTO {
    private Integer idPerfilCognitivo;
    private Integer idPaciente;
    private String inDificuldadeVisao;
    private String inUsaOculos;
    private String inDificuldadeAudicao;
    private String inUsaAparelhoAud;
    private String inDificuldadeCogn;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public PerfilCognitivoResponseDTO() {}

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