package br.com.auramed.interfaces.controllers;

import br.com.auramed.domain.service.RelatorioService;
import br.com.auramed.interfaces.dto.response.DashboardResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RelatorioControllerImpl implements RelatorioController {

    @Inject
    RelatorioService relatorioService;

    @Override
    public DashboardResponseDTO getRelatorioCompleto() {
        return relatorioService.gerarDashboardCompleto();
    }
}