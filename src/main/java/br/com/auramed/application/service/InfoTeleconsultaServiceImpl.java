package br.com.auramed.application.service;

import br.com.auramed.domain.model.InfoTeleconsulta;
import br.com.auramed.domain.repository.InfoTeleconsultaRepository;
import br.com.auramed.domain.repository.PacienteRepository;
import br.com.auramed.domain.service.InfoTeleconsultaService;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class InfoTeleconsultaServiceImpl implements InfoTeleconsultaService {

    @Inject
    InfoTeleconsultaRepository infoTeleconsultaRepository;

    @Inject
    PacienteRepository pacienteRepository;

    @Inject
    Logger logger;

    @Override
    public InfoTeleconsulta criar(InfoTeleconsulta infoTeleconsulta) {
        try {
            infoTeleconsulta.validarHabilidadeDigital();
            infoTeleconsulta.validarCanalLembrete();
            infoTeleconsulta.validarPrecisaCuidador();
            infoTeleconsulta.validarJaFezTele();
            pacienteRepository.buscarPorId(infoTeleconsulta.getIdPaciente());

            try {
                infoTeleconsultaRepository.buscarPorPaciente(infoTeleconsulta.getIdPaciente());
                throw new RuntimeException("Já existe informação de teleconsulta para este paciente");
            } catch (EntidadeNaoLocalizadaException e) {
            }

            InfoTeleconsulta infoSalva = infoTeleconsultaRepository.salvar(infoTeleconsulta);
            logger.info("InfoTeleconsulta criada com sucesso. ID: " + infoSalva.getIdInfoTeleconsulta() + " - Paciente: " + infoSalva.getIdPaciente());

            return infoSalva;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Paciente não encontrado: " + e.getMessage());
            throw new RuntimeException("Paciente não encontrado: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar infoTeleconsulta: " + e.getMessage());
            throw new RuntimeException("Falha ao criar infoTeleconsulta: " + e.getMessage());
        }
    }

    @Override
    public InfoTeleconsulta editar(Integer idInfoTeleconsulta, InfoTeleconsulta infoTeleconsulta) throws EntidadeNaoLocalizadaException {
        try {
            InfoTeleconsulta infoExistente = infoTeleconsultaRepository.buscarPorId(idInfoTeleconsulta);
            infoTeleconsulta.validarHabilidadeDigital();
            infoTeleconsulta.validarCanalLembrete();
            infoTeleconsulta.validarPrecisaCuidador();
            infoTeleconsulta.validarJaFezTele();

            if (!infoExistente.getIdPaciente().equals(infoTeleconsulta.getIdPaciente())) {
                pacienteRepository.buscarPorId(infoTeleconsulta.getIdPaciente());

                try {
                    infoTeleconsultaRepository.buscarPorPaciente(infoTeleconsulta.getIdPaciente());
                    throw new RuntimeException("Já existe informação de teleconsulta para este paciente");
                } catch (EntidadeNaoLocalizadaException e) {
                }
            }

            infoTeleconsulta.setIdInfoTeleconsulta(idInfoTeleconsulta);
            InfoTeleconsulta infoAtualizada = infoTeleconsultaRepository.editar(infoTeleconsulta);
            logger.info("InfoTeleconsulta atualizada com sucesso. ID: " + idInfoTeleconsulta);

            return infoAtualizada;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("InfoTeleconsulta não encontrada para edição. ID: " + idInfoTeleconsulta);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar infoTeleconsulta. ID: " + idInfoTeleconsulta + ": " + e.getMessage());
            throw new RuntimeException("Falha ao editar infoTeleconsulta: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer idInfoTeleconsulta) throws EntidadeNaoLocalizadaException {
        try {
            infoTeleconsultaRepository.remover(idInfoTeleconsulta);
            logger.info("InfoTeleconsulta removida com sucesso. ID: " + idInfoTeleconsulta);

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("InfoTeleconsulta não encontrada para remoção. ID: " + idInfoTeleconsulta);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover infoTeleconsulta. ID: " + idInfoTeleconsulta + ": " + e.getMessage());
            throw new RuntimeException("Falha ao remover infoTeleconsulta: " + e.getMessage());
        }
    }

    @Override
    public InfoTeleconsulta localizarPorId(Integer idInfoTeleconsulta) throws EntidadeNaoLocalizadaException {
        try {
            InfoTeleconsulta infoTeleconsulta = infoTeleconsultaRepository.buscarPorId(idInfoTeleconsulta);
            logger.info("InfoTeleconsulta localizada. ID: " + idInfoTeleconsulta);
            return infoTeleconsulta;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("InfoTeleconsulta não localizada. ID: " + idInfoTeleconsulta);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar infoTeleconsulta. ID: " + idInfoTeleconsulta + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar infoTeleconsulta: " + e.getMessage());
        }
    }

    @Override
    public InfoTeleconsulta localizarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        try {
            pacienteRepository.buscarPorId(idPaciente);

            InfoTeleconsulta infoTeleconsulta = infoTeleconsultaRepository.buscarPorPaciente(idPaciente);
            logger.info("InfoTeleconsulta localizada para paciente: " + idPaciente);
            return infoTeleconsulta;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("InfoTeleconsulta não localizada para paciente: " + idPaciente);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar infoTeleconsulta por paciente. ID Paciente: " + idPaciente + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar infoTeleconsulta por paciente: " + e.getMessage());
        }
    }

    @Override
    public List<InfoTeleconsulta> listarTodos() {
        try {
            List<InfoTeleconsulta> infoTeleconsultas = infoTeleconsultaRepository.buscarTodos();
            logger.info("Listadas " + infoTeleconsultas.size() + " infoTeleconsultas");
            return infoTeleconsultas;

        } catch (Exception e) {
            logger.error("Erro ao listar infoTeleconsultas: " + e.getMessage());
            throw new RuntimeException("Falha ao listar infoTeleconsultas: " + e.getMessage());
        }
    }

    @Override
    public void removerPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        try {
            InfoTeleconsulta info = infoTeleconsultaRepository.buscarPorPaciente(idPaciente);
            infoTeleconsultaRepository.remover(info.getIdInfoTeleconsulta());
            logger.info("Info teleconsulta removida para paciente: " + idPaciente);
        } catch (EntidadeNaoLocalizadaException e) {
            logger.debug("Info teleconsulta não encontrada para paciente: " + idPaciente);
            throw e;
        }
    }
}