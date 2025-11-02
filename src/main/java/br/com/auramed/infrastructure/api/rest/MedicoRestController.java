package br.com.auramed.infrastructure.api.rest;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.interfaces.controllers.MedicoController;
import br.com.auramed.interfaces.controllers.PacienteController;
import br.com.auramed.interfaces.dto.request.MedicoRequestDTO;
import br.com.auramed.interfaces.dto.response.MedicoResponseDTO;
import br.com.auramed.interfaces.dto.response.PacienteResponseDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicoRestController {
    private final MedicoController medicoController;

    @Inject
    PacienteController pacienteController;

    @Inject
    public MedicoRestController(MedicoController medicoController) {
        this.medicoController = medicoController;
    }

    @GET
    @Path("/{idMedico}/pacientes")
    public Response buscarPacientesPorMedico(@PathParam("idMedico") Integer idMedico) {
        try {
            List<PacienteResponseDTO> pacientes = pacienteController.getPacientesPorMedico(idMedico);
            return Response.ok(pacientes).build();
        } catch (EntidadeNaoLocalizadaException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Integer id) {
        try {
            MedicoResponseDTO medico = medicoController.getMedicoById(id);
            return Response.ok(medico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/crm/{crm}")
    public Response buscarPorCrm(@PathParam("crm") String crm) {
        try {
            MedicoResponseDTO medico = medicoController.getMedicoByCrm(crm);
            return Response.ok(medico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response buscarTodosMedicos() {
        try {
            List<MedicoResponseDTO> medicos = medicoController.getAllMedicos();
            return Response.ok(medicos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/especialidade/{idEspecialidade}")
    public Response buscarMedicosPorEspecialidade(@PathParam("idEspecialidade") Integer idEspecialidade) {
        try {
            List<MedicoResponseDTO> medicos = medicoController.getMedicosPorEspecialidade(idEspecialidade);
            return Response.ok(medicos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response criarMedico(MedicoRequestDTO medicoRequest) {
        try {
            MedicoResponseDTO medico = medicoController.criarMedico(medicoRequest);
            return Response.status(Response.Status.CREATED).entity(medico).build();
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarMedico(@PathParam("id") Integer id, MedicoRequestDTO medicoRequest) {
        try {
            MedicoResponseDTO medico = medicoController.editarMedico(id, medicoRequest);
            return Response.status(Response.Status.OK).entity(medico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarMedico(@PathParam("id") Integer id) {
        try {
            medicoController.deleteMedico(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (EntidadeNaoLocalizadaException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{id}/teleconsulta/{status}")
    public Response alterarStatusTeleconsulta(@PathParam("id") Integer id, @PathParam("status") String status) {
        try {
            MedicoResponseDTO medico = medicoController.alterarStatusTeleconsulta(id, status);
            return Response.ok(medico).build();
        } catch (EntidadeNaoLocalizadaException e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}