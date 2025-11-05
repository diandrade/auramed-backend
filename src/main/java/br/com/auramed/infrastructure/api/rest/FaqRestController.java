package br.com.auramed.infrastructure.api.rest;

import br.com.auramed.application.service.CategoriaTester;
import br.com.auramed.domain.model.PerguntaFrequente;
import br.com.auramed.domain.service.FaqService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/api/faq")
public class FaqRestController {

    @Inject
    FaqService faqService;

    @Inject
    Logger logger;

    @Inject
    CategoriaTester categoriaTester;

    @GET
    @Path("/perguntas-frequentes")
    public Response getPerguntasFrequentes(@QueryParam("limite") Integer limite) {
        try {
            int limiteFinal = limite != null ? limite : 10;
            logger.info("Buscando " + limiteFinal + " perguntas frequentes");

            List<PerguntaFrequente> perguntas = faqService.buscarPerguntasFrequentes(limiteFinal);

            return Response.ok(perguntas).build();

        } catch (Exception e) {
            logger.error("Erro no endpoint de FAQ: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar perguntas frequentes")
                    .build();
        }
    }

    @GET
    @Path("/testar-categorias")
    public Response testarCategorizacao() {
        try {
            categoriaTester.testarCategorizacao();
            return Response.ok("Teste de categorização executado. Verifique os logs do servidor.").build();
        } catch (Exception e) {
            logger.error("Erro no teste de categorização: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro no teste: " + e.getMessage())
                    .build();
        }
    }
}