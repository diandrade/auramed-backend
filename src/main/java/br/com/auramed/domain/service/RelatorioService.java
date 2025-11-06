package br.com.auramed.domain.service;

import br.com.auramed.interfaces.dto.response.RelatorioResponseDTO;

public interface RelatorioService {
    RelatorioResponseDTO gerarRelatorioCompleto();
}