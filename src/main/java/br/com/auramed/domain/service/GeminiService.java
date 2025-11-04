package br.com.auramed.domain.service;

import br.com.auramed.domain.model.BaseConhecimento;

public interface GeminiService {
    String gerarResposta(String pergunta, BaseConhecimento contexto);
    String testarConexao();
}