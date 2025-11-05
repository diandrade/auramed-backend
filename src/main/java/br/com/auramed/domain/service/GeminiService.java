package br.com.auramed.domain.service;

import br.com.auramed.domain.model.BaseConhecimento;

public interface GeminiService {

    String gerarResposta(String pergunta, BaseConhecimento contexto);

    String testarConexao();

    default String categorizarPergunta(String pergunta) {
        if (pergunta.toLowerCase().contains("agendar") || pergunta.toLowerCase().contains("marcar")) {
            return "AGENDAMENTO";
        } else if (pergunta.toLowerCase().contains("horário") || pergunta.toLowerCase().contains("funciona")) {
            return "HORARIO_FUNCIONAMENTO";
        } else if (pergunta.toLowerCase().contains("contato") || pergunta.toLowerCase().contains("telefone") || pergunta.toLowerCase().contains("email")) {
            return "CONTATO";
        } else if (pergunta.toLowerCase().contains("preparar") || pergunta.toLowerCase().contains("documento") || pergunta.toLowerCase().contains("exame")) {
            return "ORIENTACAO_PRE_CONSULTA";
        } else if (pergunta.toLowerCase().contains("simular") || pergunta.toLowerCase().contains("como funciona")) {
            return "SIMULACAO_TELECONSULTA";
        } else {
            return "INFORMACAO_GERAL";
        }
    }
}