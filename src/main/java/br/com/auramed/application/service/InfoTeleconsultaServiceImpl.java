package br.com.auramed.application.service;

import br.com.auramed.domain.model.InfoTeleconsulta;
import br.com.auramed.domain.repository.InfoTeleconsultaRepository;
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
    Logger logger;

    @Override
    public InfoTeleconsulta criar(InfoTeleconsulta infoTeleconsulta) {
        try {
            logger.info("INICIANDO CRIAÇÃO DE INFO TELECONSULTA");

            infoTeleconsulta.validarHabilidadeDigital();
            infoTeleconsulta.validarCanalLembrete();
            infoTeleconsulta.validarPrecisaCuidador();
            infoTeleconsulta.validarJaFezTele();

            InfoTeleconsulta infoTeleconsultaSalva = infoTeleconsultaRepository.salvar(infoTeleconsulta);
            logger.info("INFO TELECONSULTA CRIADA COM SUCESSO - ID: " + infoTeleconsultaSalva.getIdInfoTeleconsulta());

            return infoTeleconsultaSalva;

        } catch (Exception e) {
            logger.error("ERRO AO CRIAR INFO TELECONSULTA: " + e.getMessage());
            throw new RuntimeException("Falha ao criar info teleconsulta: " + e.getMessage());
        }
    }

    @Override
    public InfoTeleconsulta editar(Integer idInfoTeleconsulta, InfoTeleconsulta infoTeleconsulta) throws EntidadeNaoLocalizadaException {
        try {
            InfoTeleconsulta infoTeleconsultaExistente = infoTeleconsultaRepository.buscarPorId(idInfoTeleconsulta);

            infoTeleconsulta.validarHabilidadeDigital();
            infoTeleconsulta.validarCanalLembrete();
            infoTeleconsulta.validarPrecisaCuidador();
            infoTeleconsulta.validarJaFezTele();

            infoTeleconsulta.setIdInfoTeleconsulta(idInfoTeleconsulta);
            InfoTeleconsulta infoTeleconsultaAtualizada = infoTeleconsultaRepository.editar(infoTeleconsulta);
            logger.info("InfoTeleconsulta atualizada com sucesso. ID: " + idInfoTeleconsulta);

            return infoTeleconsultaAtualizada;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("InfoTeleconsulta não encontrada para edição. ID: " + idInfoTeleconsulta);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar infoTeleconsulta. ID: " + idInfoTeleconsulta + ": " + e.getMessage());
            throw new RuntimeException("Falha ao editar info teleconsulta: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer idInfoTeleconsulta) throws EntidadeNaoLocalizadaException {
        try {
            InfoTeleconsulta infoTeleconsulta = infoTeleconsultaRepository.buscarPorId(idInfoTeleconsulta);

            infoTeleconsultaRepository.remover(idInfoTeleconsulta);
            logger.info("InfoTeleconsulta removida com sucesso. ID: " + idInfoTeleconsulta);

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("InfoTeleconsulta não encontrada para remoção. ID: " + idInfoTeleconsulta);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover infoTeleconsulta. ID: " + idInfoTeleconsulta + ": " + e.getMessage());
            throw new RuntimeException("Falha ao remover info teleconsulta: " + e.getMessage());
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
            throw new RuntimeException("Falha ao localizar info teleconsulta: " + e.getMessage());
        }
    }

    @Override
    public InfoTeleconsulta localizarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        try {
            InfoTeleconsulta infoTeleconsulta = infoTeleconsultaRepository.buscarPorPaciente(idPaciente);
            logger.info("InfoTeleconsulta localizada para paciente. ID Paciente: " + idPaciente);
            return infoTeleconsulta;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("InfoTeleconsulta não localizada para paciente. ID Paciente: " + idPaciente);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar infoTeleconsulta por paciente. ID Paciente: " + idPaciente + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar info teleconsulta por paciente: " + e.getMessage());
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
            throw new RuntimeException("Falha ao listar info teleconsultas: " + e.getMessage());
        }
    }

    @Override
    public void removerPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        try {
            InfoTeleconsulta infoTeleconsulta = infoTeleconsultaRepository.buscarPorPaciente(idPaciente);
            infoTeleconsultaRepository.remover(infoTeleconsulta.getIdInfoTeleconsulta());
            logger.info("InfoTeleconsulta removida para paciente: " + idPaciente);

        } catch (EntidadeNaoLocalizadaException e) {
            logger.debug("Nenhuma infoTeleconsulta encontrada para paciente: " + idPaciente);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover infoTeleconsulta por paciente " + idPaciente + ": " + e.getMessage());
            throw new RuntimeException("Falha ao remover info teleconsulta por paciente: " + e.getMessage());
        }
    }
}