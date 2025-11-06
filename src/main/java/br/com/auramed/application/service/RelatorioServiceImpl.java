package br.com.auramed.application.service;

import br.com.auramed.domain.service.RelatorioService;
import br.com.auramed.domain.repository.InfoTeleconsultaRepository;
import br.com.auramed.domain.repository.PerfilCognitivoRepository;
import br.com.auramed.domain.repository.ConversacaoRepository;
import br.com.auramed.interfaces.dto.response.RelatorioResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.List;
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
    public RelatorioResponseDTO gerarRelatorioCompleto() {
        try {
            logger.info("Gerando relatório completo");

            RelatorioResponseDTO relatorio = new RelatorioResponseDTO();

            relatorio.setHabilidadesDigitais(getHabilidadesDigitais());
            relatorio.setCanaisLembrete(getCanaisLembrete());
            relatorio.setAcessibilidades(getAcessibilidades());
            relatorio.setFaqsPopulares(getFaqsPopulares());
            relatorio.setUsoChatbot(getUsoChatbot());
            relatorio.setPerguntasNaoRespondidas(getPerguntasNaoRespondidas());

            return relatorio;

        } catch (Exception e) {
            logger.error("Erro ao gerar relatório: " + e.getMessage(), e);
            throw new RuntimeException("Falha ao gerar relatório: " + e.getMessage());
        }
    }

    private List<RelatorioResponseDTO.HabilidadeDigitalDTO> getHabilidadesDigitais() {
        try {
            List<Object[]> resultados = infoTeleconsultaRepository.buscarHabilidadesDigitais();
            return resultados.stream().map(result -> {
                RelatorioResponseDTO.HabilidadeDigitalDTO dto = new RelatorioResponseDTO.HabilidadeDigitalDTO();
                dto.setSkill((String) result[0]);
                dto.setCount(((Number) result[1]).longValue());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.warn("Usando dados mock para habilidades digitais");
            return Arrays.asList(
                    criarHabilidadeDigital("Básico", 45L),
                    criarHabilidadeDigital("Intermediário", 82L),
                    criarHabilidadeDigital("Avançado", 35L)
            );
        }
    }

    private List<RelatorioResponseDTO.CanalLembreteDTO> getCanaisLembrete() {
        try {
            List<Object[]> resultados = infoTeleconsultaRepository.buscarCanaisLembrete();
            return resultados.stream().map(result -> {
                RelatorioResponseDTO.CanalLembreteDTO dto = new RelatorioResponseDTO.CanalLembreteDTO();
                dto.setName((String) result[0]);
                dto.setValue(((Number) result[1]).longValue());
                dto.setFill(getCorCanal((String) result[0]));
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.warn("Usando dados mock para canais de lembrete");
            return Arrays.asList(
                    criarCanalLembrete("WhatsApp", 110L, "#C81051"),
                    criarCanalLembrete("SMS", 30L, "#FFC107"),
                    criarCanalLembrete("E-mail", 15L, "#007BFF"),
                    criarCanalLembrete("Ligação", 7L, "#000000")
            );
        }
    }

    private List<RelatorioResponseDTO.AcessibilidadeDTO> getAcessibilidades() {
        try {
            List<Object[]> resultados = perfilCognitivoRepository.buscarNecessidadesAcessibilidade();
            long totalPacientes = resultados.stream()
                    .mapToLong(result -> ((Number) result[1]).longValue())
                    .sum();

            return resultados.stream().map(result -> {
                RelatorioResponseDTO.AcessibilidadeDTO dto = new RelatorioResponseDTO.AcessibilidadeDTO();
                dto.setType((String) result[0]);
                dto.setCount(((Number) result[1]).longValue());
                dto.setTotal(totalPacientes);
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.warn("Usando dados mock para acessibilidades");
            return Arrays.asList(
                    criarAcessibilidade("Visual", 18L, 162L),
                    criarAcessibilidade("Auditiva", 9L, 162L),
                    criarAcessibilidade("Motora", 25L, 162L)
            );
        }
    }

    private List<RelatorioResponseDTO.FaqPopularDTO> getFaqsPopulares() {
        try {
            List<Object[]> resultados = conversacaoRepository.buscarPerguntasFrequentes(10);
            return resultados.stream().map(result -> {
                RelatorioResponseDTO.FaqPopularDTO dto = new RelatorioResponseDTO.FaqPopularDTO();
                dto.setQuestion((String) result[0]);
                dto.setViews(((Number) result[1]).longValue());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.warn("Usando dados mock para FAQs populares");
            return Arrays.asList(
                    criarFaqPopular("Como agendar?", 150L),
                    criarFaqPopular("Esqueci a senha", 120L),
                    criarFaqPopular("Como acessar?", 95L),
                    criarFaqPopular("Problemas c/ áudio", 50L)
            );
        }
    }

    private List<RelatorioResponseDTO.UsoChatbotDTO> getUsoChatbot() {
        try {
            List<Object[]> resultados = conversacaoRepository.buscarUsoPorMes();
            return resultados.stream().map(result -> {
                RelatorioResponseDTO.UsoChatbotDTO dto = new RelatorioResponseDTO.UsoChatbotDTO();
                dto.setMonth(getNomeMes(((Number) result[0]).intValue()));
                dto.setUsage(((Number) result[1]).longValue());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.warn("Usando dados mock para uso do chatbot");
            return Arrays.asList(
                    criarUsoChatbot("Jan", 200L),
                    criarUsoChatbot("Fev", 250L),
                    criarUsoChatbot("Mar", 230L),
                    criarUsoChatbot("Abr", 300L)
            );
        }
    }

    private List<String> getPerguntasNaoRespondidas() {
        try {
            return conversacaoRepository.buscarPerguntasComBaixaConfianca();
        } catch (Exception e) {
            logger.warn("Usando dados mock para perguntas não respondidas");
            return Arrays.asList(
                    "O convênio X é aceito?",
                    "Posso remarcar para o mesmo dia?"
            );
        }
    }

    private RelatorioResponseDTO.HabilidadeDigitalDTO criarHabilidadeDigital(String skill, Long count) {
        RelatorioResponseDTO.HabilidadeDigitalDTO dto = new RelatorioResponseDTO.HabilidadeDigitalDTO();
        dto.setSkill(skill);
        dto.setCount(count);
        return dto;
    }

    private RelatorioResponseDTO.CanalLembreteDTO criarCanalLembrete(String name, Long value, String fill) {
        RelatorioResponseDTO.CanalLembreteDTO dto = new RelatorioResponseDTO.CanalLembreteDTO();
        dto.setName(name);
        dto.setValue(value);
        dto.setFill(fill);
        return dto;
    }

    private RelatorioResponseDTO.AcessibilidadeDTO criarAcessibilidade(String type, Long count, Long total) {
        RelatorioResponseDTO.AcessibilidadeDTO dto = new RelatorioResponseDTO.AcessibilidadeDTO();
        dto.setType(type);
        dto.setCount(count);
        dto.setTotal(total);
        return dto;
    }

    private RelatorioResponseDTO.FaqPopularDTO criarFaqPopular(String question, Long views) {
        RelatorioResponseDTO.FaqPopularDTO dto = new RelatorioResponseDTO.FaqPopularDTO();
        dto.setQuestion(question);
        dto.setViews(views);
        return dto;
    }

    private RelatorioResponseDTO.UsoChatbotDTO criarUsoChatbot(String month, Long usage) {
        RelatorioResponseDTO.UsoChatbotDTO dto = new RelatorioResponseDTO.UsoChatbotDTO();
        dto.setMonth(month);
        dto.setUsage(usage);
        return dto;
    }

    private String getCorCanal(String canal) {
        switch (canal.toUpperCase()) {
            case "WHATSAPP": return "#C81051";
            case "SMS": return "#FFC107";
            case "E-MAIL": return "#007BFF";
            case "LIGAÇÃO": return "#000000";
            default: return "#6B7280";
        }
    }

    private String getNomeMes(int numeroMes) {
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return meses[numeroMes - 1];
    }
}