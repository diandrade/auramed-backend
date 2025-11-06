package br.com.auramed.interfaces.dto.request;

public class PerfilCognitivoRequestDTO {
    private String inDificuldadeVisao;
    private String inUsaOculos;
    private String inDificuldadeAudicao;
    private String inUsaAparelhoAud;
    private String inDificuldadeCogn;

    public PerfilCognitivoRequestDTO() {}

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

    public Integer getIdPaciente() {
        return null;
    }
}