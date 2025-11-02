package br.com.auramed.application.service;

import br.com.auramed.domain.model.Cuidador;
import br.com.auramed.domain.repository.CuidadorRepository;
import br.com.auramed.domain.repository.PessoaRepository;
import br.com.auramed.domain.service.CuidadorService;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class CuidadorServiceImpl implements CuidadorService {

    @Inject
    CuidadorRepository cuidadorRepository;

    @Inject
    PessoaRepository pessoaRepository;

    @Inject
    Logger logger;

    @Override
    public Cuidador criar(Cuidador cuidador) {
        try {
            cuidador.validarParentesco();
            cuidador.validarTempoCuidado();

            var pessoa = pessoaRepository.buscarPorId(cuidador.getIdPessoa());
            if (!"CUIDADOR".equals(pessoa.getTipoPessoa())) {
                throw new RuntimeException("A pessoa deve ser do tipo CUIDADOR");
            }

            Cuidador cuidadorSalvo = cuidadorRepository.salvar(cuidador);
            logger.info("Cuidador criado com sucesso. ID Pessoa: " + cuidadorSalvo.getIdPessoa());

            return cuidadorSalvo;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Pessoa não encontrada: " + e.getMessage());
            throw new RuntimeException("Pessoa não encontrada: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar cuidador: " + e.getMessage());
            throw new RuntimeException("Falha ao criar cuidador: " + e.getMessage());
        }
    }

    @Override
    public Cuidador editar(Integer idPessoa, Cuidador cuidador) throws EntidadeNaoLocalizadaException {
        try {
            Cuidador cuidadorExistente = cuidadorRepository.buscarPorId(idPessoa);
            cuidador.validarParentesco();
            cuidador.validarTempoCuidado();

            cuidador.setIdPessoa(idPessoa);
            Cuidador cuidadorAtualizado = cuidadorRepository.editar(cuidador);
            logger.info("Cuidador atualizado com sucesso. ID Pessoa: " + idPessoa);

            return cuidadorAtualizado;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Cuidador não encontrado para edição. ID Pessoa: " + idPessoa);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar cuidador. ID Pessoa: " + idPessoa + ": " + e.getMessage());
            throw new RuntimeException("Falha ao editar cuidador: " + e.getMessage());
        }
    }

    @Override
    public Cuidador remover(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        try {
            Cuidador cuidador = cuidadorRepository.buscarPorId(idPessoa);

            cuidadorRepository.remover(idPessoa);
            logger.info("Cuidador removido com sucesso. ID Pessoa: " + idPessoa);

            return cuidador;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Cuidador não encontrado para remoção. ID Pessoa: " + idPessoa);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover cuidador. ID Pessoa: " + idPessoa + ": " + e.getMessage());
            throw new RuntimeException("Falha ao remover cuidador: " + e.getMessage());
        }
    }

    @Override
    public Cuidador localizar(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        try {
            Cuidador cuidador = cuidadorRepository.buscarPorId(idPessoa);
            logger.info("Cuidador localizado. ID Pessoa: " + idPessoa);
            return cuidador;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Cuidador não localizado. ID Pessoa: " + idPessoa);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar cuidador. ID Pessoa: " + idPessoa + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar cuidador: " + e.getMessage());
        }
    }

    @Override
    public List<Cuidador> listarTodos() {
        try {
            List<Cuidador> cuidadores = cuidadorRepository.buscarTodos();
            logger.info("Listados " + cuidadores.size() + " cuidadores");
            return cuidadores;

        } catch (Exception e) {
            logger.error("Erro ao listar cuidadores: " + e.getMessage());
            throw new RuntimeException("Falha ao listar cuidadores: " + e.getMessage());
        }
    }

    @Override
    public Cuidador ativar(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        try {
            Cuidador cuidador = cuidadorRepository.buscarPorId(idPessoa);
            cuidador.setAtivo("S");

            Cuidador cuidadorAtualizado = cuidadorRepository.editar(cuidador);
            logger.info("Cuidador ativado com sucesso. ID Pessoa: " + idPessoa);

            return cuidadorAtualizado;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Cuidador não encontrado para ativação. ID Pessoa: " + idPessoa);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao ativar cuidador. ID Pessoa: " + idPessoa + ": " + e.getMessage());
            throw new RuntimeException("Falha ao ativar cuidador: " + e.getMessage());
        }
    }

    @Override
    public Cuidador inativar(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        try {
            Cuidador cuidador = cuidadorRepository.buscarPorId(idPessoa);
            cuidador.setAtivo("N");

            Cuidador cuidadorAtualizado = cuidadorRepository.editar(cuidador);
            logger.info("Cuidador inativado com sucesso. ID Pessoa: " + idPessoa);

            return cuidadorAtualizado;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Cuidador não encontrado para inativação. ID Pessoa: " + idPessoa);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao inativar cuidador. ID Pessoa: " + idPessoa + ": " + e.getMessage());
            throw new RuntimeException("Falha ao inativar cuidador: " + e.getMessage());
        }
    }
}