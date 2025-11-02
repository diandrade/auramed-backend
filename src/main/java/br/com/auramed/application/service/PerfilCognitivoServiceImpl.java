package br.com.auramed.application.service;

import br.com.auramed.domain.model.PerfilCognitivo;
import br.com.auramed.domain.repository.PerfilCognitivoRepository;
import br.com.auramed.domain.repository.PacienteRepository;
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
    PacienteRepository pacienteRepository;

    @Inject
    Logger logger;

    @Override
    public PerfilCognitivo criar(PerfilCognitivo perfilCognitivo) {
        try {
            perfilCognitivo.validarIndicadores();
            pacienteRepository.buscarPorId(perfilCognitivo.getIdPaciente());

            try {
                perfilCognitivoRepository.buscarPorPaciente(perfilCognitivo.getIdPaciente());
                throw new RuntimeException("Já existe perfil cognitivo para este paciente");
            } catch (EntidadeNaoLocalizadaException ignored) {
            }

            PerfilCognitivo perfilSalvo = perfilCognitivoRepository.salvar(perfilCognitivo);
            logger.info("Perfil cognitivo criado com sucesso. ID: " + perfilSalvo.getIdPerfilCognitivo() + " - Paciente: " + perfilSalvo.getIdPaciente());

            return perfilSalvo;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Paciente não encontrado: " + e.getMessage());
            throw new RuntimeException("Paciente não encontrado: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao criar perfil cognitivo: " + e.getMessage());
            throw new RuntimeException("Falha ao criar perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public PerfilCognitivo editar(Integer idPerfilCognitivo, PerfilCognitivo perfilCognitivo) throws EntidadeNaoLocalizadaException {
        try {
            PerfilCognitivo perfilExistente = perfilCognitivoRepository.buscarPorId(idPerfilCognitivo);
            perfilCognitivo.validarIndicadores();

            if (!perfilExistente.getIdPaciente().equals(perfilCognitivo.getIdPaciente())) {
                pacienteRepository.buscarPorId(perfilCognitivo.getIdPaciente());

                try {
                    perfilCognitivoRepository.buscarPorPaciente(perfilCognitivo.getIdPaciente());
                    throw new RuntimeException("Já existe perfil cognitivo para este paciente");
                } catch (EntidadeNaoLocalizadaException e) {
                }
            }

            perfilCognitivo.setIdPerfilCognitivo(idPerfilCognitivo);
            PerfilCognitivo perfilAtualizado = perfilCognitivoRepository.editar(perfilCognitivo);
            logger.info("Perfil cognitivo atualizado com sucesso. ID: " + idPerfilCognitivo);

            return perfilAtualizado;
        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Perfil cognitivo não encontrado para edição. ID: " + idPerfilCognitivo);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar perfil cognitivo. ID: " + idPerfilCognitivo + ": " + e.getMessage());
            throw new RuntimeException("Falha ao editar perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException {
        try {
            perfilCognitivoRepository.remover(idPerfilCognitivo);
            logger.info("Perfil cognitivo removido com sucesso. ID: " + idPerfilCognitivo);

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Perfil cognitivo não encontrado para remoção. ID: " + idPerfilCognitivo);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao remover perfil cognitivo. ID: " + idPerfilCognitivo + ": " + e.getMessage());
            throw new RuntimeException("Falha ao remover perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public PerfilCognitivo localizarPorId(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException {
        try {
            PerfilCognitivo perfilCognitivo = perfilCognitivoRepository.buscarPorId(idPerfilCognitivo);
            logger.info("Perfil cognitivo localizado. ID: " + idPerfilCognitivo);
            return perfilCognitivo;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Perfil cognitivo não localizado. ID: " + idPerfilCognitivo);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar perfil cognitivo. ID: " + idPerfilCognitivo + ": " + e.getMessage());
            throw new RuntimeException("Falha ao localizar perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public PerfilCognitivo localizarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        try {
            pacienteRepository.buscarPorId(idPaciente);

            PerfilCognitivo perfilCognitivo = perfilCognitivoRepository.buscarPorPaciente(idPaciente);
            logger.info("Perfil cognitivo localizado para paciente: " + idPaciente);
            return perfilCognitivo;

        } catch (EntidadeNaoLocalizadaException e) {
            logger.error("Perfil cognitivo não localizado para paciente: " + idPaciente);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao localizar perfil cognitivo por paciente. ID Paciente: " + idPaciente + ": " + e.getMessage());
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
            PerfilCognitivo perfil = perfilCognitivoRepository.buscarPorPaciente(idPaciente);
            perfilCognitivoRepository.remover(perfil.getIdPerfilCognitivo());
            logger.info("Perfil cognitivo removido para paciente: " + idPaciente);
        } catch (EntidadeNaoLocalizadaException e) {
            logger.debug("Perfil cognitivo não encontrado para paciente: " + idPaciente);
            throw e;
        }
    }
}