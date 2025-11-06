package br.com.auramed.infrastructure.api.rest;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.*;
import br.com.auramed.domain.service.*;
import br.com.auramed.interfaces.controllers.PacienteCompletoController;
import br.com.auramed.interfaces.dto.request.PacienteCompletoRequestDTO;
import br.com.auramed.interfaces.dto.response.PacienteCompletoResponseDTO;
import br.com.auramed.interfaces.mappers.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/pacientes-completo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteCompletoRestController {
    private final PacienteCompletoController pacienteCompletoController;

    @Inject
    AuthenticationService authenticationService;

    @Inject
    PacienteService pacienteService;

    @Inject
    PessoaService pessoaService;

    @Inject
    InfoTeleconsultaService infoTeleconsultaService;

    @Inject
    PerfilCognitivoService perfilCognitivoService;

    @Inject
    PessoaMapper pessoaMapper;

    @Inject
    PacienteMapper pacienteMapper;

    @Inject
    InfoTeleconsultaMapper infoTeleconsultaMapper;

    @Inject
    PerfilCognitivoMapper perfilCognitivoMapper;

    @Inject
    public PacienteCompletoRestController(PacienteCompletoController pacienteCompletoController) {
        this.pacienteCompletoController = pacienteCompletoController;
    }

    @GET
    @Path("/{idPaciente}")
    public Response buscarPacienteCompleto(@PathParam("idPaciente") Integer idPaciente) {
        try {
            PacienteCompletoResponseDTO pacienteCompleto = pacienteCompletoController.getPacienteCompleto(idPaciente);
            return Response.ok(pacienteCompleto).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response criarPacienteCompleto(PacienteCompletoRequestDTO pacienteCompletoRequest) {
        try {
            PacienteCompletoResponseDTO pacienteCompleto = pacienteCompletoController.criarPacienteCompleto(pacienteCompletoRequest);
            return Response.status(Response.Status.CREATED).entity(pacienteCompleto).build();
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response listarPacientesCompletosDoMedicoLogado() {
        try {
            Integer idMedicoLogado = authenticationService.getMedicoLogadoId();
            Medico medicoLogado = authenticationService.getMedicoLogado();

            List<Paciente> pacientes = pacienteService.listarPorMedico(idMedicoLogado);
            List<PacienteCompletoResponseDTO> pacientesCompletos = new ArrayList<>();

            for (Paciente paciente : pacientes) {
                try {
                    Pessoa pessoa = pessoaService.localizar(paciente.getIdPessoa());

                    PacienteCompletoResponseDTO pacienteCompleto = new PacienteCompletoResponseDTO();
                    pacienteCompleto.setPessoa(pessoaMapper.toResponseDTO(pessoa));
                    pacienteCompleto.setPaciente(pacienteMapper.toResponseDTO(paciente));

                    try {
                        InfoTeleconsulta infoTeleconsulta = infoTeleconsultaService.localizarPorPaciente(paciente.getIdPessoa());
                        pacienteCompleto.setInfoTeleconsulta(infoTeleconsultaMapper.toResponseDTO(infoTeleconsulta));
                    } catch (EntidadeNaoLocalizadaException e) {
                    }


                    try {
                        PerfilCognitivo perfilCognitivo = perfilCognitivoService.localizarPorPaciente(paciente.getIdPessoa());
                        pacienteCompleto.setPerfilCognitivo(perfilCognitivoMapper.toResponseDTO(perfilCognitivo));
                    } catch (EntidadeNaoLocalizadaException e) {
                    }

                    pacientesCompletos.add(pacienteCompleto);

                } catch (EntidadeNaoLocalizadaException e) {
                }
            }

            return Response.ok(pacientesCompletos).build();

        } catch (EntidadeNaoLocalizadaException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao listar pacientes: " + e.getMessage()).build();
        }
    }
}