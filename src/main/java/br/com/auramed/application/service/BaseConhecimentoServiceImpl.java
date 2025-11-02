package br.com.auramed.application.service;

import br.com.auramed.domain.model.BaseConhecimento;
import br.com.auramed.domain.service.BaseConhecimentoService;
import br.com.auramed.domain.repository.BaseConhecimentoRepository;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class BaseConhecimentoServiceImpl implements BaseConhecimentoService {

    @Inject
    BaseConhecimentoRepository baseConhecimentoRepository;

    @Inject
    Logger logger;

    @Override
    public BaseConhecimento buscarMelhorResposta(String pergunta) {
        try {
            logger.info("Buscando melhor resposta para: " + pergunta);

            BaseConhecimento resposta = baseConhecimentoRepository
                    .buscarPorPalavrasChave(pergunta);

            if (resposta != null) {
                logger.info("Resposta encontrada por palavras-chave");
                return resposta;
            }

            resposta = baseConhecimentoRepository.buscarPorSimilaridade(pergunta);

            if (resposta != null) {
                logger.info("Resposta encontrada por similaridade");
            } else {
                logger.info("Nenhuma resposta encontrada na base de conhecimento");
            }

            return resposta;

        } catch (Exception e) {
            logger.error("Erro ao buscar resposta para: " + pergunta + ": " + e.getMessage());
            throw new RuntimeException("Falha ao buscar resposta: " + e.getMessage());
        }
    }

    @Override
    public BaseConhecimento criar(BaseConhecimento conhecimento) {
        try {
            if (conhecimento.getPergunta() == null || conhecimento.getPergunta().trim().isEmpty()) {
                throw new RuntimeException("Pergunta não pode ser vazia");
            }
            if (conhecimento.getResposta() == null || conhecimento.getResposta().trim().isEmpty()) {
                throw new RuntimeException("Resposta não pode ser vazia");
            }
            if (conhecimento.getCategoria() == null || conhecimento.getCategoria().trim().isEmpty()) {
                throw new RuntimeException("Categoria não pode ser vazia");
            }

            BaseConhecimento conhecimentoSalvo = baseConhecimentoRepository.salvar(conhecimento);
            logger.info("BaseConhecimento criada com sucesso. ID: " + conhecimentoSalvo.getId());

            return conhecimentoSalvo;

        } catch (Exception e) {
            logger.error("Erro ao criar BaseConhecimento: " + e.getMessage());
            throw new RuntimeException("Falha ao criar base de conhecimento: " + e.getMessage());
        }
    }

    @Override
    public BaseConhecimento editar(String id, BaseConhecimento conhecimento) throws EntidadeNaoLocalizadaException {
        try {
            BaseConhecimento conhecimentoExistente = baseConhecimentoRepository.buscarPorId(id);

            if (conhecimento.getPergunta() == null || conhecimento.getPergunta().trim().isEmpty()) {
                throw new RuntimeException("Pergunta não pode ser vazia");
            }
            if (conhecimento.getResposta() == null || conhecimento.getResposta().trim().isEmpty()) {
                throw new RuntimeException("Resposta não pode ser vazia");
            }
            if (conhecimento.getCategoria() == null || conhecimento.getCategoria().trim().isEmpty()) {
                throw new RuntimeException("Categoria não pode ser vazia");
            }

            conhecimento.setId(id);
            BaseConhecimento conhecimentoAtualizado = baseConhecimentoRepository.editar(conhecimento);
            logger.info("BaseConhecimento atualizada com sucesso. ID: " + id);

            return conhecimentoAtualizado;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("BaseConhecimento não encontrada para edição. ID: " + id);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar BaseConhecimento. ID: " + id + ": " + e.getMessage());
            throw new RuntimeException("Falha ao editar base de conhecimento: " + e.getMessage());
        }
    }

    @Override
    public BaseConhecimento remover(String id) throws EntidadeNaoLocalizadaException {
        try {
            BaseConhecimento conhecimento = baseConhecimentoRepository.buscarPorId(id);

            baseConhecimentoRepository.remover(id);
            logger.info("BaseConhecimento removida com sucesso. ID: " + id);

            return conhecimento;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("BaseConhecimento não encontrada para remoção. ID: " + id);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover BaseConhecimento. ID: " + id + ": " + e.getMessage());
            throw new RuntimeException("Falha ao remover base de conhecimento: " + e.getMessage());
        }
    }

    @Override
    public BaseConhecimento localizar(String id) throws EntidadeNaoLocalizadaException {
        try {
            BaseConhecimento conhecimento = baseConhecimentoRepository.buscarPorId(id);
            logger.info("BaseConhecimento localizada. ID: " + id);
            return conhecimento;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("BaseConhecimento não localizada. ID: " + id);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar BaseConhecimento. ID: " + id + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar base de conhecimento: " + e.getMessage());
        }
    }

    @Override
    public List<BaseConhecimento> listarTodos() {
        try {
            List<BaseConhecimento> conhecimentos = baseConhecimentoRepository.buscarTodos();
            logger.info("Listadas " + conhecimentos.size() + " entradas na base de conhecimento");
            return conhecimentos;

        } catch (Exception e) {
            logger.error("Erro ao listar base de conhecimento: " + e.getMessage());
            throw new RuntimeException("Falha ao listar base de conhecimento: " + e.getMessage());
        }
    }

    @Override
    public List<BaseConhecimento> listarPorCategoria(String categoria) {
        try {
            List<BaseConhecimento> conhecimentos = baseConhecimentoRepository.buscarPorCategoria(categoria);
            logger.info("Listadas " + conhecimentos.size() + " entradas na categoria: " + categoria);
            return conhecimentos;

        } catch (Exception e) {
            logger.error("Erro ao listar base de conhecimento por categoria " + categoria + ": " + e.getMessage());
            throw new RuntimeException("Falha ao listar base de conhecimento por categoria: " + e.getMessage());
        }
    }
}