package br.com.auramed.application.service;

import br.com.auramed.domain.model.BaseConhecimento;
import br.com.auramed.domain.service.GeminiAiService;
import br.com.auramed.domain.service.GeminiService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class GeminiServiceImpl implements GeminiService {

    @Inject
    GeminiAiService geminiAiService;

    @Inject
    Logger logger;

    @Override
    public String gerarResposta(String pergunta, BaseConhecimento contexto) {
        try {
            logger.info("Gerando resposta com Gemini para: " + pergunta);

            String perguntaComContexto = construirPerguntaComContexto(pergunta, contexto);
            String resposta = geminiAiService.responderPergunta(perguntaComContexto);

            logger.info("Resposta gerada com sucesso pelo Gemini");
            return resposta;

        } catch (Exception e) {
            logger.error("Erro ao gerar resposta com Gemini: " + e.getMessage());
            return tratarRespostaFallback(pergunta, contexto);
        }
    }

    @Override
    public String categorizarPergunta(String pergunta) {
        try {
            return geminiAiService.categorizarPergunta(pergunta);
        } catch (Exception e) {
            logger.warn("Usando categorização fallback para: " + pergunta);
            return GeminiService.super.categorizarPergunta(pergunta);
        }
    }

    private String construirPerguntaComContexto(String pergunta, BaseConhecimento contexto) {
        StringBuilder builder = new StringBuilder();

        if (contexto != null && contexto.getResposta() != null) {
            builder.append("Contexto relevante: ").append(contexto.getResposta()).append("\n\n");
        }

        builder.append("Pergunta do paciente: ").append(pergunta);
        return builder.toString();
    }

    private String tratarRespostaFallback(String pergunta, BaseConhecimento contexto) {
        logger.warn("Usando fallback devido a erro no Gemini");

        if (contexto != null && contexto.getConfianca() > 0.5) {
            return contexto.getResposta();
        }

        return "Desculpe, no momento nosso sistema está processando muitas solicitações. " +
                "Para agendamentos ou dúvidas urgentes, entre em contato pelo telefone (11) 5180-7800 " +
                "ou e-mail centrodepesquisa.imrea@hc.fm.usp.br";
    }

    @Override
    public String testarConexao() {
        try {
            String resposta = geminiAiService.responderPergunta("Responda apenas com 'CONECTADO'");
            logger.info("Teste de conexão com Gemini: SUCCESS");
            return "Conexão OK - Resposta: " + resposta;
        } catch (Exception e) {
            logger.error("Teste de conexão com Gemini: FAILED - " + e.getMessage());
            return "Falha na conexão: " + e.getMessage();
        }
    }
}