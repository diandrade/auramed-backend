package br.com.auramed.domain.service;

import br.com.auramed.interfaces.dto.response.DashboardResponseDTO;

public interface RelatorioService {
    DashboardResponseDTO gerarDashboardCompleto();
    DashboardResponseDTO.ProntidaoAcessibilidadeDTO getDadosProntidaoAcessibilidade();
    DashboardResponseDTO.SuporteEngajamentoDTO getDadosSuporteEngajamento();
    DashboardResponseDTO.MetricasChatbotDTO getMetricasChatbot();
}