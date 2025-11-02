package br.com.auramed.application.service;

import br.com.auramed.domain.model.Medico;
import br.com.auramed.domain.repository.MedicoRepository;
import br.com.auramed.domain.service.MedicoService;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class MedicoServiceImpl implements MedicoService {

    @Inject
    MedicoRepository medicoRepository;

    @Inject
    Logger logger;

    @Override
    public Medico criar(Medico medico) {
        try {
            medico.validar();

            if (existeMedicoComCrm(medico.getCrm())) {
                throw new RuntimeException("Já existe médico cadastrado com este CRM: " + medico.getCrm());
            }

            Medico medicoSalvo = medicoRepository.salvar(medico);
            logger.info("Médico criado com sucesso. ID: " + medicoSalvo.getId() +
                    " - CRM: " + medicoSalvo.getCrm() +
                    " - Nome: " + (medicoSalvo.getPessoa() != null ? medicoSalvo.getPessoa().getNome() : "N/A"));

            return medicoSalvo;

        } catch (Exception e) {
            logger.error("Erro ao criar médico: " + e.getMessage());
            throw new RuntimeException("Falha ao criar médico: " + e.getMessage());
        }
    }

    @Override
    public Medico editar(Integer id, Medico medico) throws EntidadeNaoLocalizadaException {
        try {
            Medico medicoExistente = medicoRepository.buscarPorId(id);
            medico.validar();

            if (!medicoExistente.getCrm().equals(medico.getCrm()) &&
                    existeMedicoComCrm(medico.getCrm())) {
                throw new RuntimeException("Já existe médico cadastrado com este CRM: " + medico.getCrm());
            }

            medico.setId(id);
            Medico medicoAtualizado = medicoRepository.editar(medico);
            logger.info("Médico atualizado com sucesso. ID: " + id +
                    " - Nome: " + (medicoAtualizado.getPessoa() != null ? medicoAtualizado.getPessoa().getNome() : "N/A"));

            return medicoAtualizado;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Médico não encontrado para edição. ID: " + id);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar médico. ID: " + id + ": " + e.getMessage());
            throw new RuntimeException("Falha ao editar médico: " + e.getMessage());
        }
    }

    @Override
    public Medico remover(Integer id) throws EntidadeNaoLocalizadaException {
        try {
            Medico medico = medicoRepository.buscarPorId(id);

            medicoRepository.remover(id);
            logger.info("Médico removido com sucesso. ID: " + id +
                    " - Nome: " + (medico.getPessoa() != null ? medico.getPessoa().getNome() : "N/A"));

            return medico;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Médico não encontrado para remoção. ID: " + id);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover médico. ID: " + id + ": " + e.getMessage());
            throw new RuntimeException("Falha ao remover médico: " + e.getMessage());
        }
    }

    @Override
    public Medico localizar(Integer id) throws EntidadeNaoLocalizadaException {
        try {
            Medico medico = medicoRepository.buscarPorId(id);
            logger.info("Médico localizado. ID: " + id +
                    " - Nome: " + (medico.getPessoa() != null ? medico.getPessoa().getNome() : "N/A"));
            return medico;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Médico não localizado. ID: " + id);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar médico. ID: " + id + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar médico: " + e.getMessage());
        }
    }

    @Override
    public Medico localizarPorCrm(String crm) throws EntidadeNaoLocalizadaException {
        try {
            Medico medico = medicoRepository.buscarPorCrm(crm);
            logger.info("Médico localizado por CRM: " + crm +
                    " - Nome: " + (medico.getPessoa() != null ? medico.getPessoa().getNome() : "N/A"));
            return medico;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Médico não localizado por CRM: " + crm);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar médico por CRM " + crm + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar médico por CRM: " + e.getMessage());
        }
    }

    @Override
    public List<Medico> listarTodos() {
        try {
            List<Medico> medicos = medicoRepository.buscarTodos();

            for (Medico medico : medicos) {
                logger.debug("Médico listado - ID: " + medico.getId() +
                        ", CRM: " + medico.getCrm() +
                        ", Nome: " + (medico.getPessoa() != null ? medico.getPessoa().getNome() : "NULL"));
            }

            logger.info("Listados " + medicos.size() + " médicos");
            return medicos;

        } catch (Exception e) {
            logger.error("Erro ao listar médicos: " + e.getMessage());
            throw new RuntimeException("Falha ao listar médicos: " + e.getMessage());
        }
    }

    @Override
    public List<Medico> listarPorEspecialidade(Integer idEspecialidade) {
        try {
            List<Medico> medicos = medicoRepository.buscarPorEspecialidade(idEspecialidade);
            logger.info("Listados " + medicos.size() + " médicos da especialidade ID: " + idEspecialidade);
            return medicos;

        } catch (Exception e) {
            logger.error("Erro ao listar médicos por especialidade " + idEspecialidade + ": " + e.getMessage());
            throw new RuntimeException("Falha ao listar médicos por especialidade: " + e.getMessage());
        }
    }

    @Override
    public Medico alterarStatusTeleconsulta(Integer id, String aceitaTeleconsulta) throws EntidadeNaoLocalizadaException {
        try {
            Medico medico = medicoRepository.buscarPorId(id);
            medico.setAceitaTeleconsulta(aceitaTeleconsulta);

            Medico medicoAtualizado = medicoRepository.editar(medico);
            logger.info("Status de teleconsulta alterado para " + aceitaTeleconsulta +
                    ". Médico ID: " + id +
                    " - Nome: " + (medicoAtualizado.getPessoa() != null ? medicoAtualizado.getPessoa().getNome() : "N/A"));

            return medicoAtualizado;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Médico não encontrado para alterar status de teleconsulta. ID: " + id);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao alterar status de teleconsulta. ID: " + id + ": " + e.getMessage());
            throw new RuntimeException("Falha ao alterar status de teleconsulta: " + e.getMessage());
        }
    }

    private boolean existeMedicoComCrm(String crm) {
        try {
            medicoRepository.buscarPorCrm(crm);
            return true;
        } catch (EntidadeNaoLocalizadaException e) {
            return false;
        }
    }
}