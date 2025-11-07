package br.com.auramed.domain.repository;

import br.com.auramed.domain.model.PerfilCognitivo;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import java.util.List;

public interface PerfilCognitivoRepository {
    PerfilCognitivo buscarPorId(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException;
    PerfilCognitivo buscarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException;
    List<PerfilCognitivo> buscarTodos();
    PerfilCognitivo salvar(PerfilCognitivo perfilCognitivo);
    PerfilCognitivo editar(PerfilCognitivo perfilCognitivo) throws EntidadeNaoLocalizadaException;
    void remover(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException;
    List<Object[]> buscarNecessidadesAcessibilidade();
    List<Object[]> buscarUsoAuxiliares();
    List<Object[]> buscarDadosAcessibilidade();
    Long count();
    List<Object[]> buscarEstatisticasCompletas();
}