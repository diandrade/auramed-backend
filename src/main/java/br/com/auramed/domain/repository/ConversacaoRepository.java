package br.com.auramed.domain.repository;

import br.com.auramed.domain.model.Conversacao;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import java.util.List;

public interface ConversacaoRepository {
    Conversacao salvar(Conversacao conversacao);
    Conversacao buscarPorId(Integer id) throws EntidadeNaoLocalizadaException;
    List<Conversacao> buscarPorUsuario(String usuarioId);
    List<Conversacao> buscarTodos();
    void remover(Integer id);
    List<Object[]> buscarPerguntasFrequentes(int limite);
    List<Object[]> buscarUsoPorMes();
    List<Object[]> buscarPerguntasFrequentes();
    List<String> buscarPerguntasComBaixaConfianca();
    Long getTotalConversacoes();
    List<Object[]> buscarEstatisticasSentimentos();
    List<Object[]> buscarUsoPorPeriodo(String periodo);
    List<Object[]> buscarMetricasEngajamento();
    List<Object[]> buscarEstatisticasFontesResposta();
}