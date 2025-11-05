package br.com.auramed.domain.service;

import br.com.auramed.domain.model.PerguntaFrequente;
import java.util.List;

public interface FaqService {
    List<PerguntaFrequente> buscarPerguntasFrequentes(int limite);
}