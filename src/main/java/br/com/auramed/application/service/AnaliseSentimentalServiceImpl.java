package br.com.auramed.application.service;

import br.com.auramed.domain.model.Sentimento;
import br.com.auramed.domain.service.AnaliseSentimentalService;
import br.com.auramed.domain.service.GeminiAiService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class AnaliseSentimentalServiceImpl implements AnaliseSentimentalService {

    @Inject
    GeminiAiService geminiAiService;

    @Inject
    Logger logger;

    private static final int MAX_RETRIES = 2;
    private static final long RETRY_DELAY_MS = 1000;

    @Override
    public Sentimento analisar(String texto) {
        int tentativas = 0;

        while (tentativas < MAX_RETRIES) {
            try {
                logger.info("Analisando sentimento do texto: " + texto + " (tentativa " + (tentativas + 1) + ")");
                
                String resposta = geminiAiService.analisarSentimento(texto);

                Sentimento sentimento = parseSentimento(resposta);
                logger.info("Sentimento detectado: " + sentimento);
                return sentimento;

            } catch (Exception e) {
                tentativas++;
                logger.warn("Tentativa " + tentativas + " falhou: " + e.getMessage());

                if (tentativas < MAX_RETRIES) {
                    try {
                        logger.info("Aguardando " + RETRY_DELAY_MS + "ms antes da próxima tentativa...");
                        TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        logger.warn("Todas as tentativas falharam, usando fallback para análise de sentimento");
        return analisarSentimentoFallback(texto);
    }

    private Sentimento parseSentimento(String resposta) {
        if (resposta == null) return Sentimento.NEUTRO;

        String respostaLimpa = resposta.trim().toUpperCase();

        if (respostaLimpa.contains("POSITIVO")) {
            return Sentimento.POSITIVO;
        } else if (respostaLimpa.contains("NEGATIVO")) {
            return Sentimento.NEGATIVO;
        } else {
            return Sentimento.NEUTRO;
        }
    }

    private Sentimento analisarSentimentoFallback(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return Sentimento.NEUTRO;
        }

        String textoLower = texto.toLowerCase();

        if (textoLower.matches(".*\\b(obrigad|obrigada|grato|grata|ajuda|por favor|pfvr|pfv|porfavor|obg|valeu|agradeço|excelente|ótimo|otimo|maravilha|perfeito)\\b.*")) {
            return Sentimento.POSITIVO;
        }

        else if (textoLower.matches(".*\\b(urgente|emergência|emergencia|dor|doi|dói|dores|socorro|ajuda|preocupad|ansios|nervos|medo|assustad|pânico|panico|médico|doutor|hospital|pronto socorro|ps|machuc|ferid|sangue|febre|vomit|enjoo|tontur)\\b.*")) {
            return Sentimento.NEGATIVO;
        }

        else if (textoLower.matches("^(oi|olá|ola|bom dia|boa tarde|boa noite|tudo bem|como vai|e aí).*")) {
            return Sentimento.NEUTRO;
        }
        else {
            return Sentimento.NEUTRO;
        }
    }
}