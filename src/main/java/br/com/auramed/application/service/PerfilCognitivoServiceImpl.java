package br.com.auramed.application.service;

import br.com.auramed.domain.model.PerfilCognitivo;
import br.com.auramed.domain.repository.PerfilCognitivoRepository;
import br.com.auramed.domain.service.PerfilCognitivoService;
import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PerfilCognitivoServiceImpl implements PerfilCognitivoService {

    @Inject
    PerfilCognitivoRepository perfilCognitivoRepository;

    @Inject
    Logger logger;

    @Override
    public PerfilCognitivo criar(PerfilCognitivo perfilCognitivo) {
        try {
            logger.info("INICIANDO CRIAÇÃO DE PERFIL COGNITIVO");

            perfilCognitivo.validarIndicadores();

            PerfilCognitivo perfilCognitivoSalvo = perfilCognitivoRepository.salvar(perfilCognitivo);
            logger.info("PERFIL COGNITIVO CRIADO COM SUCESSO - ID: " + perfilCognitivoSalvo.getIdPerfilCognitivo());

            return perfilCognitivoSalvo;

        } catch (Exception e) {
            logger.error("ERRO AO CRIAR PERFIL COGNITIVO: " + e.getMessage());
            throw new RuntimeException("Falha ao criar perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public PerfilCognitivo editar(Integer idPerfilCognitivo, PerfilCognitivo perfilCognitivo) throws EntidadeNaoLocalizadaException {
        try {
            PerfilCognitivo perfilCognitivoExistente = perfilCognitivoRepository.buscarPorId(idPerfilCognitivo);

            perfilCognitivo.validarIndicadores();

            perfilCognitivo.setIdPerfilCognitivo(idPerfilCognitivo);
            PerfilCognitivo perfilCognitivoAtualizado = perfilCognitivoRepository.editar(perfilCognitivo);
            logger.info("PerfilCognitivo atualizado com sucesso. ID: " + idPerfilCognitivo);

            return perfilCognitivoAtualizado;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("PerfilCognitivo não encontrado para edição. ID: " + idPerfilCognitivo);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar perfilCognitivo. ID: " + idPerfilCognitivo + ": " + e.getMessage());
            throw new RuntimeException("Falha ao editar perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException {
        try {
            PerfilCognitivo perfilCognitivo = perfilCognitivoRepository.buscarPorId(idPerfilCognitivo);

            perfilCognitivoRepository.remover(idPerfilCognitivo);
            logger.info("PerfilCognitivo removido com sucesso. ID: " + idPerfilCognitivo);

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("PerfilCognitivo não encontrado para remoção. ID: " + idPerfilCognitivo);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover perfilCognitivo. ID: " + idPerfilCognitivo + ": " + e.getMessage());
            throw new RuntimeException("Falha ao remover perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public PerfilCognitivo localizarPorId(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException {
        try {
            PerfilCognitivo perfilCognitivo = perfilCognitivoRepository.buscarPorId(idPerfilCognitivo);
            logger.info("PerfilCognitivo localizado. ID: " + idPerfilCognitivo);
            return perfilCognitivo;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("PerfilCognitivo não localizado. ID: " + idPerfilCognitivo);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar perfilCognitivo. ID: " + idPerfilCognitivo + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public PerfilCognitivo localizarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        try {
            PerfilCognitivo perfilCognitivo = perfilCognitivoRepository.buscarPorPaciente(idPaciente);
            logger.info("PerfilCognitivo localizado para paciente. ID Paciente: " + idPaciente);
            return perfilCognitivo;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("PerfilCognitivo não localizado para paciente. ID Paciente: " + idPaciente);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar perfilCognitivo por paciente. ID Paciente: " + idPaciente + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar perfil cognitivo por paciente: " + e.getMessage());
        }
    }

    @Override
    public List<PerfilCognitivo> listarTodos() {
        try {
            List<PerfilCognitivo> perfisCognitivos = perfilCognitivoRepository.buscarTodos();
            logger.info("Listados " + perfisCognitivos.size() + " perfis cognitivos");
            return perfisCognitivos;

        } catch (Exception e) {
            logger.error("Erro ao listar perfis cognitivos: " + e.getMessage());
            throw new RuntimeException("Falha ao listar perfis cognitivos: " + e.getMessage());
        }
    }

    @Override
    public void removerPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        try {
            PerfilCognitivo perfilCognitivo = perfilCognitivoRepository.buscarPorPaciente(idPaciente);
            perfilCognitivoRepository.remover(perfilCognitivo.getIdPerfilCognitivo());
            logger.info("PerfilCognitivo removido para paciente: " + idPaciente);

        } catch (EntidadeNaoLocalizadaException e) {
            logger.debug("Nenhum perfil cognitivo encontrado para paciente: " + idPaciente);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover perfil cognitivo por paciente " + idPaciente + ": " + e.getMessage());
            throw new RuntimeException("Falha ao remover perfil cognitivo por paciente: " + e.getMessage());
        }
    }
}