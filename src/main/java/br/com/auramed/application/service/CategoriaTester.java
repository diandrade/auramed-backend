package br.com.auramed.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.*;

@ApplicationScoped
public class CategoriaTester {

    @Inject
    Logger logger;

    private static final Map<String, String> TEST_CASES = createTestCases();

    private static Map<String, String> createTestCases() {
        Map<String, String> testCases = new HashMap<>();

        // CUSTOS
        testCases.put("o atendimento é gratuito?", "CUSTOS");
        testCases.put("quanto custa uma consulta?", "CUSTOS");
        testCases.put("é pago?", "CUSTOS");
        testCases.put("tem algum valor?", "CUSTOS");
        testCases.put("preciso pagar?", "CUSTOS");

        // CONTATO
        testCases.put("qual o email para mandar documentos?", "CONTATO");
        testCases.put("qual o telefone do imrea?", "CONTATO");
        testCases.put("como entro em contato?", "CONTATO");
        testCases.put("qual o whatsapp?", "CONTATO");
        testCases.put("onde fica o endereço?", "CONTATO");
        testCases.put("qual o e-mail?", "CONTATO");
        testCases.put("tem telefone?", "CONTATO");

        // DOCUMENTACAO
        testCases.put("quais documentos preciso levar?", "DOCUMENTACAO");
        testCases.put("preciso do cartão do sus?", "DOCUMENTACAO");
        testCases.put("quais exames devo levar?", "DOCUMENTACAO");
        testCases.put("o que levar na consulta?", "DOCUMENTACAO");
        testCases.put("documentação necessária?", "DOCUMENTACAO");

        // AGENDAMENTO
        testCases.put("como faço para agendar?", "AGENDAMENTO");
        testCases.put("quero marcar uma consulta", "AGENDAMENTO");
        testCases.put("como marco horário?", "AGENDAMENTO");
        testCases.put("preciso agendar consulta?", "AGENDAMENTO");
        testCases.put("marcação de consulta", "AGENDAMENTO");

        // TELECONSULTA
        testCases.put("como funciona a teleconsulta?", "TELECONSULTA");
        testCases.put("consulta online", "TELECONSULTA");
        testCases.put("atendimento virtual", "TELECONSULTA");
        testCases.put("como é a consulta por vídeo?", "TELECONSULTA");

        // HORARIO
        testCases.put("quais os horários de funcionamento?", "HORARIO");
        testCases.put("funciona aos sábados?", "HORARIO");
        testCases.put("que horas abre?", "HORARIO");
        testCases.put("que horas fecha?", "HORARIO");
        testCases.put("atendem de domingo?", "HORARIO");

        // SERVICOS
        testCases.put("tem fisioterapia?", "SERVICOS");
        testCases.put("fazem reabilitação?", "SERVICOS");
        testCases.put("oferecem psicologia?", "SERVICOS");
        testCases.put("tem atendimento para idosos?", "SERVICOS");

        // PROCEDIMENTOS
        testCases.put("como pego resultados?", "PROCEDIMENTOS");
        testCases.put("preciso de encaminhamento?", "PROCEDIMENTOS");
        testCases.put("como solicito atestado?", "PROCEDIMENTOS");
        testCases.put("onde busco exames?", "PROCEDIMENTOS");

        return testCases;
    }

    public void testarCategorizacao() {
        try {
            logger.info("🎯 INICIANDO TESTE DE CATEGORIZAÇÃO");
            FaqServiceImpl faqService = new FaqServiceImpl();

            int acertos = 0;
            int erros = 0;
            List<String> errosDetalhados = new ArrayList<>();

            for (Map.Entry<String, String> testCase : TEST_CASES.entrySet()) {
                String pergunta = testCase.getKey();
                String categoriaEsperada = testCase.getValue();

                String categoriaObtida = faqService.identificarCategoria(pergunta);

                if (categoriaEsperada.equals(categoriaObtida)) {
                    acertos++;
                    logger.info("✅ CORRETO: '" + pergunta + "' -> " + categoriaObtida);
                } else {
                    erros++;
                    String erro = "❌ ERRO: '" + pergunta + "' -> Esperado: " + categoriaEsperada + ", Obtido: " + categoriaObtida;
                    errosDetalhados.add(erro);
                    logger.error(erro);
                }
            }

            logger.info("📊 RESULTADO DO TESTE: " + acertos + " acertos, " + erros + " erros");

            if (!errosDetalhados.isEmpty()) {
                logger.error("🔍 ERROS DETALHADOS:");
                for (String erro : errosDetalhados) {
                    logger.error(erro);
                }
            }

        } catch (Exception e) {
            logger.error("❌ ERRO NO TESTE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}