package br.com.auramed.domain.service;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.PerfilCognitivo;
import java.util.List;

public interface PerfilCognitivoService {
    PerfilCognitivo criar(PerfilCognitivo perfilCognitivo);
    PerfilCognitivo editar(Integer idPerfilCognitivo, PerfilCognitivo perfilCognitivo) throws EntidadeNaoLocalizadaException;
    void remover(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException;
    PerfilCognitivo localizarPorId(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException;
    PerfilCognitivo localizarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException;
    List<PerfilCognitivo> listarTodos();
    void removerPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException;
}