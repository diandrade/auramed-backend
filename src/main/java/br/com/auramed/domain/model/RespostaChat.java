package br.com.auramed.domain.model;

public class RespostaChat {
    private String resposta;
    private CategoriaPergunta categoria;
    private Sentimento sentimento;
    private String fonteResposta;
    private String usuarioId;

    public RespostaChat(String resposta, CategoriaPergunta categoria,
                        Sentimento sentimento, String fonteResposta) {
        this.resposta = resposta;
        this.categoria = categoria;
        this.sentimento = sentimento;
        this.fonteResposta = fonteResposta;
    }

    public RespostaChat(String resposta, CategoriaPergunta categoria,
                        Sentimento sentimento, String fonteResposta, String usuarioId) {
        this.resposta = resposta;
        this.categoria = categoria;
        this.sentimento = sentimento;
        this.fonteResposta = fonteResposta;
        this.usuarioId = usuarioId;
    }

    public String getResposta() { return resposta; }
    public CategoriaPergunta getCategoria() { return categoria; }
    public Sentimento getSentimento() { return sentimento; }
    public String getFonteResposta() { return fonteResposta; }
    public String getUsuarioId() { return usuarioId; } // NOVO GETTER
    public void setResposta(String resposta) { this.resposta = resposta; }
    public void setCategoria(CategoriaPergunta categoria) { this.categoria = categoria; }
    public void setSentimento(Sentimento sentimento) { this.sentimento = sentimento; }
    public void setFonteResposta(String fonteResposta) { this.fonteResposta = fonteResposta; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; } // NOVO SETTER
}