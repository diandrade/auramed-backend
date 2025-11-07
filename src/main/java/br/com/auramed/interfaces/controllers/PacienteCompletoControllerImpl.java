package br.com.auramed.interfaces.controllers;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.*;
import br.com.auramed.domain.service.*;
import br.com.auramed.interfaces.dto.request.PacienteCompletoRequestDTO;
import br.com.auramed.interfaces.dto.response.PacienteCompletoResponseDTO;
import br.com.auramed.interfaces.mappers.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PacienteCompletoControllerImpl implements PacienteCompletoController {

    @Inject
    PessoaService pessoaService;

    @Inject
    PacienteService pacienteService;

    @Inject
    InfoTeleconsultaService infoTeleconsultaService;

    @Inject
    PerfilCognitivoService perfilCognitivoService;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    PessoaMapper pessoaMapper;

    @Inject
    PacienteMapper pacienteMapper;

    @Inject
    InfoTeleconsultaMapper infoTeleconsultaMapper;

    @Inject
    PerfilCognitivoMapper perfilCognitivoMapper;

    @Inject
    Logger logger;

    @Override
    public PacienteCompletoResponseDTO criarPacienteCompleto(PacienteCompletoRequestDTO pacienteCompletoRequest) throws EntidadeNaoLocalizadaException {
        try {
            logger.info("INICIANDO CRIAÇÃO DE PACIENTE COMPLETO");

            Pessoa pessoa = pessoaMapper.toDomain(pacienteCompletoRequest.getPessoa());
            Pessoa pessoaCriada = pessoaService.criar(pessoa);

            Integer idMedicoLogado = authenticationService.getMedicoLogadoId();
            Medico medicoLogado = authenticationService.getMedicoLogado();

            logger.info("Vinculando paciente ao médico logado: " +
                    medicoLogado.getPessoa().getNome() + " (ID: " + idMedicoLogado + ")");

            Paciente paciente = pacienteMapper.toDomain(pacienteCompletoRequest.getPaciente());
            paciente.setIdPessoa(pessoaCriada.getId());
            paciente.setIdMedicoResponsavel(idMedicoLogado);

            Paciente pacienteCriado = pacienteService.criar(paciente);

            InfoTeleconsulta infoTeleconsulta = null;
            if (pacienteCompletoRequest.getInfoTeleconsulta() != null) {
                infoTeleconsulta = infoTeleconsultaMapper.toDomain(pacienteCompletoRequest.getInfoTeleconsulta());
                infoTeleconsulta.setIdPaciente(pacienteCriado.getIdPessoa());
                infoTeleconsulta = infoTeleconsultaService.criar(infoTeleconsulta);
            }

            PerfilCognitivo perfilCognitivo = null;
            if (pacienteCompletoRequest.getPerfilCognitivo() != null) {
                perfilCognitivo = perfilCognitivoMapper.toDomain(pacienteCompletoRequest.getPerfilCognitivo());
                perfilCognitivo.setIdPaciente(pacienteCriado.getIdPessoa());
                perfilCognitivo = perfilCognitivoService.criar(perfilCognitivo);
            }

            PacienteCompletoResponseDTO response = new PacienteCompletoResponseDTO();
            response.setPessoa(pessoaMapper.toResponseDTO(pessoaCriada));
            response.setPaciente(pacienteMapper.toResponseDTO(pacienteCriado));

            if (infoTeleconsulta != null) {
                response.setInfoTeleconsulta(infoTeleconsultaMapper.toResponseDTO(infoTeleconsulta));
            }

            if (perfilCognitivo != null) {
                response.setPerfilCognitivo(perfilCognitivoMapper.toResponseDTO(perfilCognitivo));
            }

            logger.info("PACIENTE COMPLETO CRIADO COM SUCESSO - " +
                    "ID Pessoa: " + pessoaCriada.getId() +
                    " | Médico responsável: " + medicoLogado.getPessoa().getNome() +
                    " (ID: " + idMedicoLogado + ")");

            return response;

        } catch (Exception e) {
            logger.error("ERRO AO CRIAR PACIENTE COMPLETO: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public PacienteCompletoResponseDTO getPacienteCompleto(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        try {
            Integer idMedicoLogado = authenticationService.getMedicoLogadoId();
            Medico medicoLogado = authenticationService.getMedicoLogado();

            logger.info("Médico logado tentando acessar paciente - " +
                    "Médico: " + medicoLogado.getPessoa().getNome() +
                    " (ID: " + idMedicoLogado + "), " +
                    "Paciente solicitado: " + idPaciente);

            Paciente paciente = pacienteService.localizar(idPaciente);

            if (!paciente.getIdMedicoResponsavel().equals(idMedicoLogado)) {
                logger.warn("ACESSO NEGADO - Médico " + idMedicoLogado +
                        " tentou acessar paciente " + idPaciente +
                        " que pertence ao médico " + paciente.getIdMedicoResponsavel());
                throw new EntidadeNaoLocalizadaException("Paciente não encontrado ou acesso não autorizado");
            }

            Pessoa pessoa = pessoaService.localizar(paciente.getIdPessoa());

            InfoTeleconsulta infoTeleconsulta = null;
            try {
                infoTeleconsulta = infoTeleconsultaService.localizarPorPaciente(idPaciente);
            } catch (EntidadeNaoLocalizadaException e) {
            }

            PerfilCognitivo perfilCognitivo = null;
            try {
                perfilCognitivo = perfilCognitivoService.localizarPorPaciente(idPaciente);
            } catch (EntidadeNaoLocalizadaException e) {
            }

            PacienteCompletoResponseDTO response = new PacienteCompletoResponseDTO();
            response.setPessoa(pessoaMapper.toResponseDTO(pessoa));
            response.setPaciente(pacienteMapper.toResponseDTO(paciente));

            if (infoTeleconsulta != null) {
                response.setInfoTeleconsulta(infoTeleconsultaMapper.toResponseDTO(infoTeleconsulta));
            }

            if (perfilCognitivo != null) {
                response.setPerfilCognitivo(perfilCognitivoMapper.toResponseDTO(perfilCognitivo));
            }

            logger.info("ACESSO AUTORIZADO - Médico " + medicoLogado.getPessoa().getNome() +
                    " visualizou paciente " + pessoa.getNome());

            return response;

        } catch (Exception e) {
            throw e;
        }
    }
}