package br.com.auramed.domain.repository;

import br.com.auramed.domain.model.InfoTeleconsulta;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import java.util.List;

public interface InfoTeleconsultaRepository {
    InfoTeleconsulta buscarPorId(Integer idInfoTeleconsulta) throws EntidadeNaoLocalizadaException;
    InfoTeleconsulta buscarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException;
    List<InfoTeleconsulta> buscarTodos();
    InfoTeleconsulta salvar(InfoTeleconsulta infoTeleconsulta);
    InfoTeleconsulta editar(InfoTeleconsulta infoTeleconsulta) throws EntidadeNaoLocalizadaException;
    void remover(Integer idInfoTeleconsulta) throws EntidadeNaoLocalizadaException;
    List<Object[]> buscarHabilidadesDigitais();
    List<Object[]> buscarCanaisLembrete();
    List<Object[]> buscarPreferenciasCuidador();
    List<Object[]> buscarExperienciaTeleconsulta();
    Long getTotalRegistros();
}