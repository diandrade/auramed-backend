package br.com.auramed.application.service;

import br.com.auramed.domain.model.CategoriaPergunta;
import br.com.auramed.domain.service.CategorizacaoService;
import dev.langchain4j.model.chat.ChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CategorizacaoServiceImpl implements CategorizacaoService {

    @Inject
    ChatModel modeloGemini;

    @Inject
    Logger logger;

    @Override
    public CategoriaPergunta categorizar(String pergunta) {
        try {
            logger.info("Categorizando pergunta: " + (pergunta.length() > 50 ? pergunta.substring(0, 50) + "..." : pergunta));

            String promptTexto = """
            Categorize a seguinte pergunta médica em português e responda APENAS com uma destas categorias:
            PERGUNTA_MEDICA, AGENDAMENTO_CONSULTA, PRESCRICAO_MEDICAMENTOS, INFORMAÇÃO_GERAL, CUIDADOS_URGENTES, DUVIDA_PROCEDIMENTO
            
            Pergunta: "%s"
            """.formatted(pergunta);

            String resultado = modeloGemini.chat(promptTexto);

            CategoriaPergunta categoria = CategoriaPergunta.valueOf(resultado.trim().toUpperCase());
            logger.info("Pergunta categorizada como: " + categoria);
            return categoria;

        } catch (Exception e) {
            logger.error("Erro ao categorizar pergunta: " + e.getMessage());
            return CategoriaPergunta.INFORMACAO_GERAL;
        }
    }
}