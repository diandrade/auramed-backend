package br.com.auramed.application.service;

import br.com.auramed.domain.model.PacienteCuidador;
import br.com.auramed.domain.repository.PacienteCuidadorRepository;
import br.com.auramed.domain.repository.PacienteRepository;
import br.com.auramed.domain.repository.CuidadorRepository;
import br.com.auramed.domain.service.PacienteCuidadorService;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PacienteCuidadorServiceImpl implements PacienteCuidadorService {

    @Inject
    PacienteCuidadorRepository pacienteCuidadorRepository;

    @Inject
    PacienteRepository pacienteRepository;

    @Inject
    CuidadorRepository cuidadorRepository;

    @Inject
    Logger logger;

    @Override
    public PacienteCuidador associar(PacienteCuidador pacienteCuidador) {
        try {
            pacienteCuidador.validarTipoRelacionamento();
            pacienteRepository.buscarPorId(pacienteCuidador.getIdPaciente());
            cuidadorRepository.buscarPorId(pacienteCuidador.getIdCuidador());

            if (pacienteCuidadorRepository.existeAssociacao(pacienteCuidador.getIdPaciente(), pacienteCuidador.getIdCuidador())) {
                throw new RuntimeException("Associação entre paciente e cuidador já existe");
            }

            PacienteCuidador associacaoSalva = pacienteCuidadorRepository.salvar(pacienteCuidador);
            logger.info("Associação paciente-cuidador criada com sucesso. Paciente: " +
                    pacienteCuidador.getIdPaciente() + " - Cuidador: " + pacienteCuidador.getIdCuidador());

            return associacaoSalva;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Paciente ou cuidador não encontrado: " + e.getMessage());
            throw new RuntimeException("Paciente ou cuidador não encontrado: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao associar paciente-cuidador: " + e.getMessage());
            throw new RuntimeException("Falha ao associar paciente-cuidador: " + e.getMessage());
        }
    }

    @Override
    public void desassociar(Integer idPaciente, Integer idCuidador) throws EntidadeNaoLocalizadaException {
        try {
            pacienteCuidadorRepository.buscarPorIds(idPaciente, idCuidador);

            pacienteCuidadorRepository.remover(idPaciente, idCuidador);
            logger.info("Associação paciente-cuidador removida com sucesso. Paciente: " +
                    idPaciente + " - Cuidador: " + idCuidador);

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Associação não encontrada para remoção. Paciente: " + idPaciente + " - Cuidador: " + idCuidador);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao desassociar paciente-cuidador. Paciente: " + idPaciente + " - Cuidador: " + idCuidador + ": " + e.getMessage());
            throw new RuntimeException("Falha ao desassociar paciente-cuidador: " + e.getMessage());
        }
    }

    @Override
    public PacienteCuidador localizarAssociacao(Integer idPaciente, Integer idCuidador) throws EntidadeNaoLocalizadaException {
        try {
            PacienteCuidador associacao = pacienteCuidadorRepository.buscarPorIds(idPaciente, idCuidador);
            logger.info("Associação paciente-cuidador localizada. Paciente: " + idPaciente + " - Cuidador: " + idCuidador);
            return associacao;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Associação não localizada. Paciente: " + idPaciente + " - Cuidador: " + idCuidador);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar associação. Paciente: " + idPaciente + " - Cuidador: " + idCuidador + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar associação: " + e.getMessage());
        }
    }

    @Override
    public List<PacienteCuidador> listarCuidadoresDoPaciente(Integer idPaciente) {
        try {
            pacienteRepository.buscarPorId(idPaciente);

            List<PacienteCuidador> associacoes = pacienteCuidadorRepository.buscarPorPaciente(idPaciente);
            logger.info("Listados " + associacoes.size() + " cuidadores do paciente: " + idPaciente);
            return associacoes;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Paciente não encontrado. ID: " + idPaciente);
            throw new RuntimeException("Paciente não encontrado: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao listar cuidadores do paciente " + idPaciente + ": " + e.getMessage());
            throw new RuntimeException("Falha ao listar cuidadores do paciente: " + e.getMessage());
        }
    }

    @Override
    public List<PacienteCuidador> listarPacientesDoCuidador(Integer idCuidador) {
        try {
            cuidadorRepository.buscarPorId(idCuidador);

            List<PacienteCuidador> associacoes = pacienteCuidadorRepository.buscarPorCuidador(idCuidador);
            logger.info("Listados " + associacoes.size() + " pacientes do cuidador: " + idCuidador);
            return associacoes;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Cuidador não encontrado. ID: " + idCuidador);
            throw new RuntimeException("Cuidador não encontrado: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao listar pacientes do cuidador " + idCuidador + ": " + e.getMessage());
            throw new RuntimeException("Falha ao listar pacientes do cuidador: " + e.getMessage());
        }
    }

    @Override
    public List<PacienteCuidador> listarTodasAssociacoes() {
        try {
            List<PacienteCuidador> associacoes = pacienteCuidadorRepository.buscarTodos();
            logger.info("Listadas " + associacoes.size() + " associações paciente-cuidador");
            return associacoes;

        } catch (Exception e) {
            logger.error("Erro ao listar associações paciente-cuidador: " + e.getMessage());
            throw new RuntimeException("Falha ao listar associações paciente-cuidador: " + e.getMessage());
        }
    }
}