package br.com.auramed.domain.service;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.InfoTeleconsulta;
import java.util.List;

public interface InfoTeleconsultaService {
    InfoTeleconsulta criar(InfoTeleconsulta infoTeleconsulta);
    InfoTeleconsulta editar(Integer idInfoTeleconsulta, InfoTeleconsulta infoTeleconsulta) throws EntidadeNaoLocalizadaException;
    void remover(Integer idInfoTeleconsulta) throws EntidadeNaoLocalizadaException;
    InfoTeleconsulta localizarPorId(Integer idInfoTeleconsulta) throws EntidadeNaoLocalizadaException;
    InfoTeleconsulta localizarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException;
    List<InfoTeleconsulta> listarTodos();
    void removerPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException;
}