package br.com.auramed.infrastructure.api.rest;

import br.com.auramed.interfaces.controllers.ChatbotController;
import br.com.auramed.interfaces.dto.request.ChatRequestDTO;
import br.com.auramed.interfaces.dto.response.ChatResponseDTO;
import br.com.auramed.domain.service.GeminiService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatbotRestController {
    private final ChatbotController chatbotController;

    @Inject
    GeminiService geminiService;

    @Inject
    Logger logger;

    @Inject
    public ChatbotRestController(ChatbotController chatbotController) {
        this.chatbotController = chatbotController;
    }

    @POST
    public Response processarMensagem(ChatRequestDTO chatRequest) {
        try {
            logger.info("Recebida requisição de chat do usuário: " + chatRequest.getUsuarioId());

            ChatResponseDTO resposta = chatbotController.processarMensagem(chatRequest);

            logger.info("Resposta enviada com sucesso para usuário: " + chatRequest.getUsuarioId());
            return Response.ok(resposta).build();

        } catch (Exception e) {
            logger.error("Erro ao processar mensagem: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao processar mensagem: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/teste-gemini")
    public Response testeConexaoGemini() {
        try {
            String resultado = geminiService.testarConexao();
            return Response.ok("✅ " + resultado).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity("❌ ERRO: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/health")
    public Response healthCheck() {
        return Response.ok("Chatbot service is running").build();
    }
}