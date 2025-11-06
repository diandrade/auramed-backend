package br.com.auramed.domain.service;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Paciente;
import java.util.List;

public interface PacienteService {
    Paciente criar(Paciente paciente);
    Paciente editar(Integer idPessoa, Paciente paciente) throws EntidadeNaoLocalizadaException;
    Paciente remover(Integer idPessoa) throws EntidadeNaoLocalizadaException;
    Paciente localizar(Integer idPessoa) throws EntidadeNaoLocalizadaException;
    List<Paciente> listarTodos();
    List<Paciente> listarPorMedico(Integer idMedicoResponsavel);
    Paciente ativar(Integer idPessoa) throws EntidadeNaoLocalizadaException;
    Paciente inativar(Integer idPessoa) throws EntidadeNaoLocalizadaException;
    boolean pacientePertenceAoMedico(Integer idPaciente, Integer idMedico) throws EntidadeNaoLocalizadaException;
}