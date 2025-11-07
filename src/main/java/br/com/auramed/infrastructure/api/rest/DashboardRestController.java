package br.com.auramed.infrastructure.api.rest;

import br.com.auramed.domain.service.RelatorioService;
import br.com.auramed.interfaces.dto.response.DashboardResponseDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardRestController {

    @Inject
    RelatorioService relatorioService;

    @GET
    @Path("/completo")
    public Response getDashboardCompleto() {
        try {
            DashboardResponseDTO dashboard = relatorioService.gerarDashboardCompleto();
            return Response.ok(dashboard).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao gerar dashboard: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/prontidao-acessibilidade")
    public Response getProntidaoAcessibilidade() {
        try {
            DashboardResponseDTO.ProntidaoAcessibilidadeDTO dados = relatorioService.getDadosProntidaoAcessibilidade();
            return Response.ok(dados).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar dados de prontidão: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/suporte-engajamento")
    public Response getSuporteEngajamento() {
        try {
            DashboardResponseDTO.SuporteEngajamentoDTO dados = relatorioService.getDadosSuporteEngajamento();
            return Response.ok(dados).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar dados de suporte: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/metricas-chatbot")
    public Response getMetricasChatbot() {
        try {
            DashboardResponseDTO.MetricasChatbotDTO dados = relatorioService.getMetricasChatbot();
            return Response.ok(dados).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar métricas do chatbot: " + e.getMessage())
                    .build();
        }
    }
}