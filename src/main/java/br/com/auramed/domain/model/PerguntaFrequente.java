package br.com.auramed.domain.model;

public class PerguntaFrequente {
    private String pergunta;
    private String resposta;
    private Long frequencia;
    private String categoria;

    public PerguntaFrequente() {}

    public PerguntaFrequente(String pergunta, String resposta, Long frequencia, String categoria) {
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.frequencia = frequencia;
        this.categoria = categoria;
    }

    public String getPergunta() { return pergunta; }
    public void setPergunta(String pergunta) { this.pergunta = pergunta; }

    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }

    public Long getFrequencia() { return frequencia; }
    public void setFrequencia(Long frequencia) { this.frequencia = frequencia; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}