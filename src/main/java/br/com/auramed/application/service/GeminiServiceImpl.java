package br.com.auramed.application.service;

import br.com.auramed.domain.model.BaseConhecimento;
import br.com.auramed.domain.service.GeminiService;
import dev.langchain4j.model.chat.ChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class GeminiServiceImpl implements GeminiService {

    @Inject
    ChatModel modeloGemini;

    @Inject
    Logger logger;

    @Override
    public String gerarResposta(String pergunta, BaseConhecimento contexto) {
        try {
            logger.info("Gerando resposta com Gemini (Free) para: " + pergunta);

            if (pergunta.length() > 8000) {
                logger.warn("Pergunta muito longa para o plano gratuito, truncando...");
                pergunta = pergunta.substring(0, 8000);
            }

            String textoPrompt = construirPrompt(pergunta, contexto);
            String resposta = modeloGemini.chat(textoPrompt);

            logger.info("Resposta gerada com sucesso pelo Gemini Free");
            return resposta;

        } catch (Exception e) {
            logger.error("Erro ao gerar resposta com Gemini Free: " + e.getMessage());

            if (e.getMessage() != null &&
                    (e.getMessage().contains("rate limit") ||
                            e.getMessage().contains("quota"))) {
                logger.warn("Limite de taxa atingido, usando resposta padrão");
                return "No momento estou processando muitas solicitações. Por favor, tente novamente em alguns instantes ou consulte nossa base de conhecimento.";
            }

            throw new RuntimeException("Falha ao gerar resposta: " + e.getMessage());
        }
    }

    private String construirPrompt(String pergunta, BaseConhecimento contexto) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Você é um assistente médico especializado chamado AuraMed. ");
        prompt.append("Responda em português de forma clara, precisa e empática.\n\n");

        if (contexto != null) {
            prompt.append("Contexto relevante: ").append(contexto.getResposta()).append("\n\n");
        }

        prompt.append("Pergunta do paciente: ").append(pergunta).append("\n\n");
        prompt.append("Instruções: \n");
        prompt.append("- Seja preciso e baseie-se em evidências médicas\n");
        prompt.append("- Use linguagem acessível para pacientes\n");
        prompt.append("- Se não souber, diga que vai consultar um especialista\n");
        prompt.append("- Mantenha o tom profissional mas acolhedor\n");
        prompt.append("- Limite a resposta a 500 palavras\n");

        return prompt.toString();
    }
}