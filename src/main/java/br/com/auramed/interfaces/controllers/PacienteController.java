package br.com.auramed.interfaces.controllers;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.interfaces.dto.request.PacienteRequestDTO;
import br.com.auramed.interfaces.dto.response.PacienteResponseDTO;
import java.util.List;

public interface PacienteController {
    PacienteResponseDTO criarPaciente(PacienteRequestDTO pacienteRequest) throws EntidadeNaoLocalizadaException;
    PacienteResponseDTO editarPaciente(Integer idPessoa, PacienteRequestDTO pacienteRequest) throws EntidadeNaoLocalizadaException;
    PacienteResponseDTO getPacienteById(Integer idPessoa) throws EntidadeNaoLocalizadaException;
    void deletePaciente(Integer idPessoa) throws EntidadeNaoLocalizadaException;
    List<PacienteResponseDTO> getAllPacientes();
    List<PacienteResponseDTO> getPacientesPorMedico(Integer idMedico) throws EntidadeNaoLocalizadaException;
    PacienteResponseDTO ativarPaciente(Integer idPessoa) throws EntidadeNaoLocalizadaException;
    PacienteResponseDTO inativarPaciente(Integer idPessoa) throws EntidadeNaoLocalizadaException;
}