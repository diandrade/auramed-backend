package br.com.auramed.application.service;

import br.com.auramed.domain.service.RelatorioService;
import br.com.auramed.domain.repository.InfoTeleconsultaRepository;
import br.com.auramed.domain.repository.PerfilCognitivoRepository;
import br.com.auramed.domain.repository.ConversacaoRepository;
import br.com.auramed.interfaces.dto.response.DashboardResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class RelatorioServiceImpl implements RelatorioService {

    @Inject
    InfoTeleconsultaRepository infoTeleconsultaRepository;

    @Inject
    PerfilCognitivoRepository perfilCognitivoRepository;

    @Inject
    ConversacaoRepository conversacaoRepository;

    @Inject
    Logger logger;

    @Override
    public DashboardResponseDTO gerarDashboardCompleto() {
        try {
            logger.info("Gerando dashboard completo");

            DashboardResponseDTO dashboard = new DashboardResponseDTO();
            dashboard.setProntidaoAcessibilidade(getDadosProntidaoAcessibilidade());
            dashboard.setSuporteEngajamento(getDadosSuporteEngajamento());
            dashboard.setMetricasChatbot(getMetricasChatbot());

            return dashboard;

        } catch (Exception e) {
            logger.error("Erro ao gerar dashboard: " + e.getMessage(), e);
            throw new RuntimeException("Falha ao gerar dashboard: " + e.getMessage());
        }
    }

    @Override
    public DashboardResponseDTO.ProntidaoAcessibilidadeDTO getDadosProntidaoAcessibilidade() {
        try {
            DashboardResponseDTO.ProntidaoAcessibilidadeDTO dados = new DashboardResponseDTO.ProntidaoAcessibilidadeDTO();

            List<Object[]> habilidadesResult = infoTeleconsultaRepository.buscarHabilidadesDigitais();
            dados.setHabilidadesDigitais(habilidadesResult.stream().map(result -> {
                DashboardResponseDTO.HabilidadeDigitalDTO dto = new DashboardResponseDTO.HabilidadeDigitalDTO();
                dto.setSkill(convertSkillName((String) result[0]));
                dto.setCount(((Number) result[1]).longValue());
                return dto;
            }).collect(Collectors.toList()));

            List<Object[]> canaisResult = infoTeleconsultaRepository.buscarCanaisLembrete();
            dados.setCanaisLembrete(canaisResult.stream().map(result -> {
                DashboardResponseDTO.CanalLembreteDTO dto = new DashboardResponseDTO.CanalLembreteDTO();
                dto.setName(convertChannelName((String) result[0]));
                dto.setValue(((Number) result[1]).longValue());
                dto.setFill(getCorCanal((String) result[0]));
                return dto;
            }).collect(Collectors.toList()));

            List<Object[]> acessibilidadesResult = perfilCognitivoRepository.buscarNecessidadesAcessibilidade();
            Long totalPacientes = perfilCognitivoRepository.count();

            dados.setDificuldadesAcessibilidade(acessibilidadesResult.stream().map(result -> {
                DashboardResponseDTO.DificuldadeAcessibilidadeDTO dto = new DashboardResponseDTO.DificuldadeAcessibilidadeDTO();
                dto.setType((String) result[0]);
                dto.setCount(((Number) result[1]).longValue());
                dto.setTotal(totalPacientes);
                return dto;
            }).collect(Collectors.toList()));

            return dados;

        } catch (Exception e) {
            logger.warn("Erro ao buscar dados de prontidão, usando dados mock: " + e.getMessage());
            return getDadosProntidaoMock();
        }
    }

    @Override
    public DashboardResponseDTO.SuporteEngajamentoDTO getDadosSuporteEngajamento() {
        try {
            DashboardResponseDTO.SuporteEngajamentoDTO dados = new DashboardResponseDTO.SuporteEngajamentoDTO();

            List<Object[]> faqsResult = conversacaoRepository.buscarPerguntasFrequentes(10);
            List<DashboardResponseDTO.FaqPopularDTO> faqsPopulares = faqsResult.stream().map(result -> {
                DashboardResponseDTO.FaqPopularDTO dto = new DashboardResponseDTO.FaqPopularDTO();
                dto.setQuestion((String) result[0]);
                dto.setViews(((Number) result[1]).longValue());

                String categoria = identificarCategoriaFaq((String) result[0]);
                dto.setCategoria(categoria);

                return dto;
            }).collect(Collectors.toList());
            dados.setFaqsPopulares(faqsPopulares);

            List<Object[]> usoResult = conversacaoRepository.buscarUsoPorPeriodo("MONTH");
            dados.setUsoChatbot(usoResult.stream().map(result -> {
                DashboardResponseDTO.UsoChatbotDTO dto = new DashboardResponseDTO.UsoChatbotDTO();
                dto.setMonth((String) result[0]);
                dto.setUsage(((Number) result[1]).longValue());
                return dto;
            }).collect(Collectors.toList()));

            List<String> perguntasNaoRespondidas = conversacaoRepository.buscarPerguntasComBaixaConfianca();
            dados.setPerguntasNaoRespondidas(perguntasNaoRespondidas);

            return dados;

        } catch (Exception e) {
            logger.warn("Erro ao buscar dados de suporte, usando dados mock: " + e.getMessage());
            return getDadosSuporteMock();
        }
    }

    @Override
    public DashboardResponseDTO.MetricasChatbotDTO getMetricasChatbot() {
        try {
            DashboardResponseDTO.MetricasChatbotDTO metricas = new DashboardResponseDTO.MetricasChatbotDTO();

            List<Object[]> sentimentosResult = conversacaoRepository.buscarEstatisticasSentimentos();
            List<DashboardResponseDTO.EstatisticaSentimentoDTO> estatisticasSentimentos = sentimentosResult.stream().map(result -> {
                DashboardResponseDTO.EstatisticaSentimentoDTO dto = new DashboardResponseDTO.EstatisticaSentimentoDTO();
                dto.setSentimento((String) result[0]);
                dto.setQuantidade(((Number) result[1]).longValue());
                return dto;
            }).collect(Collectors.toList());
            metricas.setEstatisticasSentimentos(estatisticasSentimentos);

            List<Object[]> engajamentoResult = conversacaoRepository.buscarMetricasEngajamento();
            if (!engajamentoResult.isEmpty()) {
                Object[] metrics = engajamentoResult.get(0);
                metricas.setTotalConversas(((Number) metrics[0]).longValue());
                metricas.setUsuariosUnicos(((Number) metrics[1]).longValue());
                metricas.setMediaConversasPorUsuario(((Number) metrics[2]).doubleValue());
            }

            List<Object[]> fontesResult = conversacaoRepository.buscarEstatisticasFontesResposta();
            List<DashboardResponseDTO.FonteRespostaDTO> fontesResposta = fontesResult.stream().map(result -> {
                DashboardResponseDTO.FonteRespostaDTO dto = new DashboardResponseDTO.FonteRespostaDTO();
                dto.setFonte((String) result[0]);
                dto.setQuantidade(((Number) result[1]).longValue());
                return dto;
            }).collect(Collectors.toList());
            metricas.setFontesResposta(fontesResposta);

            return metricas;

        } catch (Exception e) {
            logger.warn("Erro ao buscar métricas do chatbot, usando dados mock: " + e.getMessage());
            return getMetricasChatbotMock();
        }
    }

    private String convertSkillName(String skill) {
        Map<String, String> skillMap = Map.of(
                "BAIXA", "Básico",
                "MEDIA", "Intermediário",
                "ALTA", "Avançado",
                "NENHUMA", "Nenhuma"
        );
        return skillMap.getOrDefault(skill, skill);
    }

    private String convertChannelName(String channel) {
        Map<String, String> channelMap = Map.of(
                "WHATSAPP", "WhatsApp",
                "SMS", "SMS",
                "EMAIL", "E-mail",
                "TELEFONE", "Ligação"
        );
        return channelMap.getOrDefault(channel, channel);
    }

    private String getCorCanal(String canal) {
        switch (canal.toUpperCase()) {
            case "WHATSAPP": return "#C81051";
            case "SMS": return "#FFC107";
            case "EMAIL": return "#007BFF";
            case "TELEFONE": return "#000000";
            default: return "#6B7280";
        }
    }

    private DashboardResponseDTO.ProntidaoAcessibilidadeDTO getDadosProntidaoMock() {
        DashboardResponseDTO.ProntidaoAcessibilidadeDTO dados = new DashboardResponseDTO.ProntidaoAcessibilidadeDTO();

        dados.setHabilidadesDigitais(Arrays.asList(
                criarHabilidadeDigital("Básico", 45L),
                criarHabilidadeDigital("Intermediário", 82L),
                criarHabilidadeDigital("Avançado", 35L)
        ));

        dados.setCanaisLembrete(Arrays.asList(
                criarCanalLembrete("WhatsApp", 110L, "#C81051"),
                criarCanalLembrete("SMS", 30L, "#FFC107"),
                criarCanalLembrete("E-mail", 15L, "#007BFF"),
                criarCanalLembrete("Ligação", 7L, "#000000")
        ));

        dados.setDificuldadesAcessibilidade(Arrays.asList(
                criarDificuldadeAcessibilidade("Visual", 18L, 162L),
                criarDificuldadeAcessibilidade("Auditiva", 9L, 162L),
                criarDificuldadeAcessibilidade("Cognitiva", 25L, 162L)
        ));

        return dados;
    }

    private DashboardResponseDTO.SuporteEngajamentoDTO getDadosSuporteMock() {
        DashboardResponseDTO.SuporteEngajamentoDTO dados = new DashboardResponseDTO.SuporteEngajamentoDTO();

        dados.setFaqsPopulares(Arrays.asList(
                criarFaqPopular("Como agendar?", 150L),
                criarFaqPopular("Esqueci a senha", 120L),
                criarFaqPopular("Como acessar?", 95L),
                criarFaqPopular("Problemas c/ áudio", 50L)
        ));

        dados.setUsoChatbot(Arrays.asList(
                criarUsoChatbot("Jan", 200L),
                criarUsoChatbot("Fev", 250L),
                criarUsoChatbot("Mar", 230L),
                criarUsoChatbot("Abr", 300L)
        ));

        dados.setPerguntasNaoRespondidas(Arrays.asList(
                "O convênio X é aceito?",
                "Posso remarcar para o mesmo dia?"
        ));

        return dados;
    }

    private DashboardResponseDTO.MetricasChatbotDTO getMetricasChatbotMock() {
        DashboardResponseDTO.MetricasChatbotDTO metricas = new DashboardResponseDTO.MetricasChatbotDTO();

        metricas.setEstatisticasSentimentos(Arrays.asList(
                criarEstatisticaSentimento("POSITIVO", 120L),
                criarEstatisticaSentimento("NEUTRO", 85L),
                criarEstatisticaSentimento("NEGATIVO", 15L)
        ));

        metricas.setTotalConversas(220L);
        metricas.setUsuariosUnicos(150L);
        metricas.setMediaConversasPorUsuario(1.47);

        metricas.setFontesResposta(Arrays.asList(
                criarFonteResposta("BASE_CONHECIMENTO", 180L),
                criarFonteResposta("GEMINI", 35L),
                criarFonteResposta("HIBRIDO", 5L)
        ));

        return metricas;
    }

    private DashboardResponseDTO.HabilidadeDigitalDTO criarHabilidadeDigital(String skill, Long count) {
        DashboardResponseDTO.HabilidadeDigitalDTO dto = new DashboardResponseDTO.HabilidadeDigitalDTO();
        dto.setSkill(skill);
        dto.setCount(count);
        return dto;
    }

    private DashboardResponseDTO.CanalLembreteDTO criarCanalLembrete(String name, Long value, String fill) {
        DashboardResponseDTO.CanalLembreteDTO dto = new DashboardResponseDTO.CanalLembreteDTO();
        dto.setName(name);
        dto.setValue(value);
        dto.setFill(fill);
        return dto;
    }

    private DashboardResponseDTO.DificuldadeAcessibilidadeDTO criarDificuldadeAcessibilidade(String type, Long count, Long total) {
        DashboardResponseDTO.DificuldadeAcessibilidadeDTO dto = new DashboardResponseDTO.DificuldadeAcessibilidadeDTO();
        dto.setType(type);
        dto.setCount(count);
        dto.setTotal(total);
        return dto;
    }

    private DashboardResponseDTO.FaqPopularDTO criarFaqPopular(String question, Long views) {
        DashboardResponseDTO.FaqPopularDTO dto = new DashboardResponseDTO.FaqPopularDTO();
        dto.setQuestion(question);
        dto.setViews(views);
        return dto;
    }

    private DashboardResponseDTO.UsoChatbotDTO criarUsoChatbot(String month, Long usage) {
        DashboardResponseDTO.UsoChatbotDTO dto = new DashboardResponseDTO.UsoChatbotDTO();
        dto.setMonth(month);
        dto.setUsage(usage);
        return dto;
    }

    private DashboardResponseDTO.EstatisticaSentimentoDTO criarEstatisticaSentimento(String sentimento, Long quantidade) {
        DashboardResponseDTO.EstatisticaSentimentoDTO dto = new DashboardResponseDTO.EstatisticaSentimentoDTO();
        dto.setSentimento(sentimento);
        dto.setQuantidade(quantidade);
        return dto;
    }

    private DashboardResponseDTO.FonteRespostaDTO criarFonteResposta(String fonte, Long quantidade) {
        DashboardResponseDTO.FonteRespostaDTO dto = new DashboardResponseDTO.FonteRespostaDTO();
        dto.setFonte(fonte);
        dto.setQuantidade(quantidade);
        return dto;
    }

    private String identificarCategoriaFaq(String pergunta) {
        if (pergunta == null) return "GERAL";

        String perguntaLower = pergunta.toLowerCase();

        if (perguntaLower.contains("agendar") || perguntaLower.contains("marcar") || perguntaLower.contains("consulta")) {
            return "AGENDAMENTO";
        }
        if (perguntaLower.contains("documento") || perguntaLower.contains("cpf") || perguntaLower.contains("rg")) {
            return "DOCUMENTACAO";
        }
        if (perguntaLower.contains("teleconsulta") || perguntaLower.contains("online") || perguntaLower.contains("virtual")) {
            return "TELECONSULTA";
        }
        if (perguntaLower.contains("horário") || perguntaLower.contains("funciona") || perguntaLower.contains("abre")) {
            return "HORARIO";
        }
        if (perguntaLower.contains("telefone") || perguntaLower.contains("email") || perguntaLower.contains("contato")) {
            return "CONTATO";
        }
        if (perguntaLower.contains("gratuito") || perguntaLower.contains("pago") || perguntaLower.contains("custo")) {
            return "CUSTOS";
        }

        return "OUTRAS_DUVIDAS";
    }
}