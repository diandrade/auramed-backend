package br.com.auramed.infrastructure.api.rest;

import br.com.auramed.interfaces.controllers.RelatorioController;
import br.com.auramed.interfaces.dto.response.RelatorioResponseDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/relatorios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelatorioRestController {

    private final RelatorioController relatorioController;

    @Inject
    public RelatorioRestController(RelatorioController relatorioController) {
        this.relatorioController = relatorioController;
    }

    @GET
    @Path("/completo")
    public Response getRelatorioCompleto() {
        try {
            RelatorioResponseDTO relatorio = relatorioController.getRelatorioCompleto();
            return Response.ok(relatorio).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao gerar relatório: " + e.getMessage())
                    .build();
        }
    }
}