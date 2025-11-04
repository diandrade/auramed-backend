package br.com.auramed.domain.service;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface GeminiAiService {

    @UserMessage("""
        Você é o AuraMed, um assistente médico especializado.
        Responda em português de forma clara, precisa e empática.
        
        Pergunta do paciente: {pergunta}
        
        Instruções:
        - Seja preciso e baseie-se em evidências médicas
        - Use linguagem acessível para pacientes  
        - Se não souber, diga que vai consultar um especialista
        - Mantenha o tom profissional mas acolhedor
        - Limite a resposta a 500 palavras
        """)
    String responderPerguntaMedica(String pergunta);
}