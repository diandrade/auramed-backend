package br.com.auramed.domain.model;

import java.time.LocalDateTime;

public class Conversacao {
    private String id;
    private String usuarioId;
    private String perguntaUsuario;
    private String respostaBot;
    private String sentimento;
    private String categoria;
    private String fonteResposta;
    private LocalDateTime dataHora;
    private Double pontuacaoSentimento;

    public Conversacao() {}

    public Conversacao(String usuarioId, String perguntaUsuario, String respostaBot,
                       String sentimento, String categoria, String fonteResposta) {
        this.usuarioId = usuarioId;
        this.perguntaUsuario = perguntaUsuario;
        this.respostaBot = respostaBot;
        this.sentimento = sentimento;
        this.categoria = categoria;
        this.fonteResposta = fonteResposta;
        this.dataHora = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getPerguntaUsuario() {
        return perguntaUsuario;
    }

    public void setPerguntaUsuario(String perguntaUsuario) {
        this.perguntaUsuario = perguntaUsuario;
    }

    public String getRespostaBot() {
        return respostaBot;
    }

    public void setRespostaBot(String respostaBot) {
        this.respostaBot = respostaBot;
    }

    public String getSentimento() {
        return sentimento;
    }

    public void setSentimento(String sentimento) {
        this.sentimento = sentimento;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFonteResposta() {
        return fonteResposta;
    }

    public void setFonteResposta(String fonteResposta) {
        this.fonteResposta = fonteResposta;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Double getPontuacaoSentimento() {
        return pontuacaoSentimento;
    }

    public void setPontuacaoSentimento(Double pontuacaoSentimento) {
        this.pontuacaoSentimento = pontuacaoSentimento;
    }
}