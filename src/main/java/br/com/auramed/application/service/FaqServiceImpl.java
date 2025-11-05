package br.com.auramed.application.service;

import br.com.auramed.domain.model.PerguntaFrequente;
import br.com.auramed.domain.service.FaqService;
import br.com.auramed.domain.repository.ConversacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class FaqServiceImpl implements FaqService {

    @Inject
    ConversacaoRepository conversacaoRepository;

    @Inject
    Logger logger;

    private static final Map<String, List<String>> TOPICOS_CHAVE = Map.of(
            "CUSTOS", Arrays.asList("gratuito", "pago", "pagamento", "custo", "valor", "preço", "preco", "cobrança", "cobranca", "dinheiro", "taxa"),
            "AGENDAMENTO", Arrays.asList("agendar", "marcar", "consulta", "marcação", "marcacao", "horário", "horario", "data", "agendamento"),
            "DOCUMENTACAO", Arrays.asList("documento", "levar", "preciso", "carteirinha", "cartão", "cartao", "cpf", "rg", "sus", "exame", "necessário", "necessario"),
            "TELECONSULTA", Arrays.asList("teleconsulta", "online", "virtual", "vídeo", "video", "internet", "digital", "remoto", "chamada", "videochamada"),
            "HORARIO", Arrays.asList("funciona", "abre", "fecha", "expediente", "atendimento", "sábado", "sabado", "domingo", "feriado", "horário", "horario"),
            "CONTATO", Arrays.asList("telefone", "email", "whatsapp", "endereço", "endereco", "local", "falar", "comunicar", "contato", "número", "numero"),
            "SERVICOS", Arrays.asList("fisioterapia", "reabilitação", "reabilitacao", "psicologia", "cadeira", "idaros", "atendimento", "serviço", "servico", "tratamento"),
            "PROCEDIMENTOS", Arrays.asList("encaminhamento", "retorno", "resultado", "exame", "receita", "atestado", "autorização", "autorizacao", "laudo")
    );

    @Override
    public List<PerguntaFrequente> buscarPerguntasFrequentes(int limite) {
        try {
            logger.info("Buscando perguntas frequentes, limite: " + limite);
            List<Object[]> resultados = conversacaoRepository.buscarPerguntasFrequentes(limite * 10);

            List<PerguntaIndividual> perguntasIndividuais = resultados.stream()
                    .filter(resultado -> isPerguntaValida((String) resultado[0]))
                    .map(resultado -> new PerguntaIndividual(
                            (String) resultado[0],
                            (Long) resultado[1],
                            identificarCategoria((String) resultado[0])
                    ))
                    .sorted((p1, p2) -> Long.compare(p2.getFrequencia(), p1.getFrequencia()))
                    .collect(Collectors.toList());

            List<PerguntaFrequente> faqs = agruparTopPerguntasPorCategoria(perguntasIndividuais, limite);

            logger.info("Encontradas " + faqs.size() + " perguntas no TOP " + limite);
            return faqs;

        } catch (Exception e) {
            logger.error("Erro ao buscar perguntas frequentes: " + e.getMessage());
            return getPerguntasPadrao();
        }
    }

    private List<PerguntaFrequente> agruparTopPerguntasPorCategoria(List<PerguntaIndividual> perguntas, int limite) {
        Map<String, List<PerguntaIndividual>> perguntasPorCategoria = new HashMap<>();

        for (PerguntaIndividual pergunta : perguntas) {
            perguntasPorCategoria
                    .computeIfAbsent(pergunta.getCategoria(), k -> new ArrayList<>())
                    .add(pergunta);
        }

        List<PerguntaFrequente> topPerguntas = new ArrayList<>();
        for (Map.Entry<String, List<PerguntaIndividual>> entry : perguntasPorCategoria.entrySet()) {
            String categoria = entry.getKey();
            List<PerguntaIndividual> perguntasDaCategoria = entry.getValue();

            PerguntaIndividual perguntaMaisFrequente = perguntasDaCategoria.stream()
                    .max(Comparator.comparingLong(PerguntaIndividual::getFrequencia))
                    .orElse(null);

            if (perguntaMaisFrequente != null) {
                String resposta = gerarRespostaPadrao(perguntaMaisFrequente.getTexto(), categoria);
                String categoriaFormatada = formatarCategoriaParaFrontend(categoria);
                topPerguntas.add(new PerguntaFrequente(
                        perguntaMaisFrequente.getTexto(),
                        resposta,
                        perguntaMaisFrequente.getFrequencia(),
                        categoriaFormatada + " (" + perguntaMaisFrequente.getFrequencia() + " " +
                                (perguntaMaisFrequente.getFrequencia() == 1 ? "vez" : "vezes") + ")"
                ));
            }
        }

        return topPerguntas.stream()
                .sorted((p1, p2) -> Long.compare(p2.getFrequencia(), p1.getFrequencia()))
                .limit(limite)
                .collect(Collectors.toList());
    }

    public String identificarCategoria(String pergunta) {
        String perguntaLower = pergunta.toLowerCase().trim();

        logger.debug("🔍 Categorizando: '" + pergunta + "'");

        if (perguntaLower.matches(".*\\b(email|e-mail|correio|eletrônico|eletronico)\\b.*") &&
                perguntaLower.matches(".*\\b(documento|arquivo|pdf|digitalizar|enviar|mandar)\\b.*")) {
            logger.debug("📧 Caso específico: Email para documentos -> CONTATO");
            return "CONTATO";
        }

        if (perguntaLower.matches(".*\\b(gratuito|pago|pagamento|custo|valor|preço|preco|dinheiro|taxa)\\b.*")) {
            logger.debug("💰 Caso específico: Pergunta sobre custos -> CUSTOS");
            return "CUSTOS";
        }

        Map<String, Integer> pontuacaoCategorias = new HashMap<>();

        if (perguntaLower.matches(".*\\b(gratuito|pago|pagamento|custo|valor|preço|preco)\\b.*")) {
            pontuacaoCategorias.put("CUSTOS", 10);
        }

        if (perguntaLower.matches(".*\\b(telefone|fone|celular|whatsapp|zap|email|e-mail|correio|endereço|endereco|local|rua|avenida)\\b.*")) {
            pontuacaoCategorias.put("CONTATO", 8);
        }

        if (perguntaLower.matches(".*\\b(agendar|marcar|consulta|marcação|marcacao|horário|horario|data|agenda)\\b.*")) {
            pontuacaoCategorias.merge("AGENDAMENTO", 5, Integer::sum);
        }

        if (perguntaLower.matches(".*\\b(documento|levar|preciso|trazer|carteirinha|carteira|cartão|cartao|cpf|rg|identidade|sus)\\b.*")) {
            pontuacaoCategorias.merge("DOCUMENTACAO", 5, Integer::sum);
        }

        if (perguntaLower.matches(".*\\b(teleconsulta|online|virtual|vídeo|video|internet|digital|remoto|chamada|videochamada|zoom|meet)\\b.*")) {
            pontuacaoCategorias.merge("TELECONSULTA", 5, Integer::sum);
        }

        if (perguntaLower.matches(".*\\b(horário|horario|funciona|abre|fecha|expediente|atendimento|segunda|terça|terca|quarta|quinta|sexta|sábado|sabado|domingo|feriado)\\b.*")) {
            pontuacaoCategorias.merge("HORARIO", 4, Integer::sum);
        }

        if (perguntaLower.matches(".*\\b(fisioterapia|reabilitação|reabilitacao|psicologia|psicólogo|psicologo|terapia|cadeira|idoso|idosa|atendimento|serviço|servico|tratamento)\\b.*")) {
            pontuacaoCategorias.merge("SERVICOS", 4, Integer::sum);
        }

        if (perguntaLower.matches(".*\\b(encaminhamento|retorno|resultado|exame|receita|atestado|autorização|autorizacao|laudo|pedido)\\b.*")) {
            pontuacaoCategorias.merge("PROCEDIMENTOS", 4, Integer::sum);
        }

        if (pontuacaoCategorias.isEmpty()) {
            logger.debug("📝 Categoria padrão: OUTRAS_DUVIDAS");
            return "OUTRAS_DUVIDAS";
        }

        String categoriaFinal = pontuacaoCategorias.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        logger.debug("🎯 Categoria final: " + categoriaFinal + " (pontuação: " + pontuacaoCategorias.get(categoriaFinal) + ")");
        return categoriaFinal;
    }

    private String formatarCategoriaParaFrontend(String categoria) {
        Map<String, String> formatos = Map.of(
                "AGENDAMENTO", "Agendamento",
                "DOCUMENTACAO", "Documentação",
                "TELECONSULTA", "Teleconsulta",
                "HORARIO", "Horário de Funcionamento",
                "CONTATO", "Contato",
                "CUSTOS", "Custos e Valores",
                "SERVICOS", "Serviços",
                "PROCEDIMENTOS", "Procedimentos",
                "OUTRAS_DUVIDAS", "Outras Dúvidas"
        );

        return formatos.getOrDefault(categoria, categoria);
    }

    private boolean isPerguntaValida(String pergunta) {
        if (pergunta == null || pergunta.trim().isEmpty()) {
            return false;
        }

        String perguntaLimpa = pergunta.trim().toLowerCase();

        if (perguntaLimpa.length() < 8) {
            return false;
        }

        if (perguntaLimpa.matches("^(oi|ola|olá|hello|tchau|obrigado|obrigada|valeu)[\\s\\.,!?]*$")) {
            return false;
        }

        return true;
    }

    private String gerarRespostaPadrao(String pergunta, String categoria) {
        switch (categoria) {
            case "AGENDAMENTO":
                return "📅 Para agendar uma consulta, entre em contato pelo telefone (11) 5180-7800 ou através do nosso WhatsApp. Nossa equipe terá prazer em ajudar você!";
            case "DOCUMENTACAO":
                return "📄 Para consultas: RG, CPF, Cartão do SUS e exames recentes. Para teleconsultas: digitalize esses documentos antes da consulta.";
            case "TELECONSULTA":
                return "💻 Sim! Oferecemos teleconsultas por videochamada. Você precisa de dispositivo com câmera, microfone e internet. Entre em contato para agendar.";
            case "HORARIO":
                return "⏰ Funcionamos de segunda a sábado, das 7h às 19h. Aos domingos e feriados estamos fechados.";
            case "CONTATO":
                return "📞 Telefone: (11) 5180-7800 | WhatsApp: mesmo número | E-mail: centrodepesquisa.imrea@hc.fm.usp.br";
            case "CUSTOS":
                return "💰 Atendemos principalmente pelo SUS (gratuito). Para informações sobre planos de saúde ou particular, entre em contato conosco.";
            case "SERVICOS":
                return "🏥 Oferecemos reabilitação física, fisioterapia, psicologia e atendimento especializado. Entre em contato para mais informações.";
            case "PROCEDIMENTOS":
                return "📋 Para exames, retornos, receitas e encaminhamentos, consulte nossa equipe pelo telefone (11) 5180-7800.";
            default:
                return "ℹ️ Para mais informações sobre esta questão, entre em contato conosco pelo telefone (11) 5180-7800.";
        }
    }

    private List<PerguntaFrequente> getPerguntasPadrao() {
        return Arrays.asList(
                new PerguntaFrequente("Como agendar uma consulta?",
                        "📅 Entre em contato pelo telefone (11) 5180-7800 ou WhatsApp.", 1L, "Agendamento (1 vez)"),
                new PerguntaFrequente("Quais documentos preciso levar?",
                        "📄 RG, CPF, Cartão do SUS e exames recentes.", 1L, "Documentação (1 vez)"),
                new PerguntaFrequente("Como funciona a teleconsulta?",
                        "💻 Atendimento por videochamada. Precisa de dispositivo com câmera e internet.", 1L, "Teleconsulta (1 vez)"),
                new PerguntaFrequente("Quais são os horários de funcionamento?",
                        "⏰ Segunda a sábado, das 7h às 19h.", 1L, "Horário de Funcionamento (1 vez)"),
                new PerguntaFrequente("Como entro em contato com o IMREA?",
                        "📞 Telefone: (11) 5180-7800 | WhatsApp | E-mail", 1L, "Contato (1 vez)")
        );
    }

    private static class PerguntaIndividual {
        private final String texto;
        private final long frequencia;
        private final String categoria;

        public PerguntaIndividual(String texto, long frequencia, String categoria) {
            this.texto = texto;
            this.frequencia = frequencia;
            this.categoria = categoria;
        }

        public String getTexto() { return texto; }
        public long getFrequencia() { return frequencia; }
        public String getCategoria() { return categoria; }
    }
}