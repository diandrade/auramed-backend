package br.com.auramed.interfaces.controllers;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Paciente;
import br.com.auramed.domain.service.PacienteService;
import br.com.auramed.interfaces.dto.request.PacienteRequestDTO;
import br.com.auramed.interfaces.dto.response.PacienteResponseDTO;
import br.com.auramed.interfaces.mappers.PacienteMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PacienteControllerImpl implements PacienteController {
    private final PacienteService pacienteService;
    private final PacienteMapper pacienteMapper;

    @Inject
    public PacienteControllerImpl(PacienteService pacienteService, PacienteMapper pacienteMapper) {
        this.pacienteService = pacienteService;
        this.pacienteMapper = pacienteMapper;
    }

    @Override
    public PacienteResponseDTO criarPaciente(PacienteRequestDTO pacienteRequest) throws EntidadeNaoLocalizadaException {
        try {
            Paciente paciente = pacienteMapper.toDomain(pacienteRequest);
            Paciente pacienteCriado = this.pacienteService.criar(paciente);
            return pacienteMapper.toResponseDTO(pacienteCriado);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public PacienteResponseDTO editarPaciente(Integer idPessoa, PacienteRequestDTO pacienteRequest) throws EntidadeNaoLocalizadaException {
        Paciente pacienteExistente = this.pacienteService.localizar(idPessoa);

        pacienteExistente.setIdMedicoResponsavel(pacienteRequest.getIdMedicoResponsavel());
        pacienteExistente.setNrCartaoSUS(pacienteRequest.getNrCartaoSUS());

        Paciente pacienteAtualizado = this.pacienteService.editar(idPessoa, pacienteExistente);
        return pacienteMapper.toResponseDTO(pacienteAtualizado);
    }

    @Override
    public PacienteResponseDTO getPacienteById(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        Paciente paciente = this.pacienteService.localizar(idPessoa);
        return pacienteMapper.toResponseDTO(paciente);
    }

    @Override
    public void deletePaciente(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        this.pacienteService.remover(idPessoa);
    }

    @Override
    public List<PacienteResponseDTO> getAllPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodos();
        return pacienteMapper.toResponseDTOList(pacientes);
    }

    @Override
    public List<PacienteResponseDTO> getPacientesPorMedico(Integer idMedico) throws EntidadeNaoLocalizadaException {
        List<Paciente> pacientes = pacienteService.listarPorMedico(idMedico);
        return pacienteMapper.toResponseDTOList(pacientes);
    }

    @Override
    public PacienteResponseDTO ativarPaciente(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        Paciente pacienteAtivado = this.pacienteService.ativar(idPessoa);
        return pacienteMapper.toResponseDTO(pacienteAtivado);
    }

    @Override
    public PacienteResponseDTO inativarPaciente(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        Paciente pacienteInativado = this.pacienteService.inativar(idPessoa);
        return pacienteMapper.toResponseDTO(pacienteInativado);
    }
}