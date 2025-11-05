package br.com.auramed.application.service;

import br.com.auramed.domain.model.Conversacao;
import br.com.auramed.domain.model.BaseConhecimento;
import br.com.auramed.domain.model.CategoriaPergunta;
import br.com.auramed.domain.model.Sentimento;
import br.com.auramed.domain.model.RespostaChat;
import br.com.auramed.domain.service.*;
import br.com.auramed.domain.repository.ConversacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ChatbotServiceImpl implements ChatbotService {

    @Inject
    BaseConhecimentoService baseConhecimentoService;

    @Inject
    GeminiService geminiService;

    @Inject
    AnaliseSentimentalService analiseSentimentalService;

    @Inject
    ConversacaoRepository conversacaoRepository;

    @Inject
    Logger logger;

    @Override
    public RespostaChat processarPergunta(String usuarioId, String perguntaUsuario) {
        try {
            logger.info("Processando pergunta do usuário: " + usuarioId + " - " + perguntaUsuario);

            BaseConhecimento melhorResposta = baseConhecimentoService
                    .buscarMelhorResposta(perguntaUsuario);

            String respostaFinal;
            String fonteResposta;
            CategoriaPergunta categoria;

            if (melhorResposta != null && melhorResposta.getConfianca() > 0.85) {
                respostaFinal = melhorResposta.getResposta();
                fonteResposta = "BASE_CONHECIMENTO";
                categoria = converterStringParaCategoria(melhorResposta.getCategoria());
                logger.info("Resposta encontrada na base de conhecimento");
            } else {
                respostaFinal = geminiService.gerarResposta(perguntaUsuario, melhorResposta);
                fonteResposta = "GEMINI";
                String categoriaString = geminiService.categorizarPergunta(perguntaUsuario);
                categoria = converterStringParaCategoria(categoriaString);
                logger.info("Resposta gerada pelo Gemini");
            }

            Sentimento sentimento = analiseSentimentalService.analisar(perguntaUsuario);

            Conversacao conversa = new Conversacao();
            conversa.setUsuarioId(usuarioId);
            conversa.setPerguntaUsuario(perguntaUsuario);
            conversa.setRespostaBot(respostaFinal);
            conversa.setSentimento(sentimento.name());
            conversa.setCategoria(categoria.name());
            conversa.setFonteResposta(fonteResposta);

            conversacaoRepository.salvar(conversa);
            logger.info("Conversa salva com sucesso para usuário: " + usuarioId);

            return new RespostaChat(respostaFinal, categoria, sentimento, fonteResposta, usuarioId);

        } catch (Exception e) {
            logger.error("Erro ao processar pergunta do usuário " + usuarioId + ": " + e.getMessage());
            return new RespostaChat(
                    "Desculpe, estou com dificuldades técnicas. " +
                            "Por favor, entre em contato conosco pelo telefone (11) 5180-7800 " +
                            "ou e-mail centrodepesquisa.imrea@hc.fm.usp.br",
                    CategoriaPergunta.ERRO,
                    Sentimento.NEUTRO,
                    "SISTEMA",
                    usuarioId
            );
        }
    }

    private CategoriaPergunta converterStringParaCategoria(String categoriaString) {
        if (categoriaString == null) {
            return CategoriaPergunta.INFORMACAO_GERAL;
        }

        try {
            return CategoriaPergunta.valueOf(categoriaString);
        } catch (IllegalArgumentException e) {
            return switch (categoriaString.toUpperCase()) {
                case "AGENDAMENTO" -> CategoriaPergunta.AGENDAMENTO;
                case "CONTATO" -> CategoriaPergunta.INFORMACAO_GERAL;
                case "ORIENTACAO_PRE_CONSULTA" -> CategoriaPergunta.ORIENTACAO;
                case "SIMULACAO_TELECONSULTA" -> CategoriaPergunta.ORIENTACAO;
                case "INFORMACAO_GERAL" -> CategoriaPergunta.INFORMACAO_GERAL;
                default -> CategoriaPergunta.INFORMACAO_GERAL;
            };
        }
    }
}