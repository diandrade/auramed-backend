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
            String resposta = geminiAiService.responderPerguntaMedica(perguntaComContexto);

            logger.info("Resposta gerada com sucesso pelo Gemini");
            return resposta;

        } catch (Exception e) {
            logger.error("Erro ao gerar resposta com Gemini: " + e.getMessage());
            return tratarRespostaFallback(pergunta, contexto);
        }
    }

    private String construirPerguntaComContexto(String pergunta, BaseConhecimento contexto) {
        if (contexto != null && contexto.getResposta() != null) {
            return "Contexto: " + contexto.getResposta() + "\n\nPergunta: " + pergunta;
        }
        return pergunta;
    }

    private String tratarRespostaFallback(String pergunta, BaseConhecimento contexto) {
        logger.warn("Usando fallback devido a erro no Gemini");

        if (contexto != null && contexto.getConfianca() > 0.5) {
            return contexto.getResposta();
        }

        return "Desculpe, no momento nosso sistema está processando muitas solicitações. " +
                "Por favor, tente novamente em alguns minutos.";
    }

    @Override
    public String testarConexao() {
        try {
            String resposta = geminiAiService.responderPerguntaMedica("Responda apenas com 'OK'");
            logger.info("Teste de conexão com Gemini: SUCCESS");
            return "Conexão OK - Resposta: " + resposta;
        } catch (Exception e) {
            logger.error("Teste de conexão com Gemini: FAILED - " + e.getMessage());
            return "Falha na conexão: " + e.getMessage();
        }
    }
}