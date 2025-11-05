package br.com.auramed.domain.service;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface GeminiAiService {

    @UserMessage("""
        Você é a Clara, assistente virtual do IMREA especializada em telemedicina.
        Responda em português de forma clara, precisa e empática.
        
        Contexto do IMREA:
        - Instituto de Medicina Física e Reabilitação
        - Oferece teleconsultas e atendimentos presenciais
        - Horário de funcionamento: 7h às 19h (segunda a sábado)
        - Contato: (11) 5180-7800 | centrodepesquisa.imrea@hc.fm.usp.br
        
        Pergunta do paciente: {pergunta}
        
        Instruções:
        - Foque em orientações sobre teleconsultas, agendamentos e informações do IMREA
        - Seja preciso e baseie-se nas informações institucionais
        - Use linguagem acessível para pacientes de todas as idades
        - Se não souber, oriente sobre os canais oficiais de contato
        - Mantenha o tom profissional mas acolhedor
        - Limite a resposta a 300 palavras
        - Inclua orientações práticas quando relevante
        """)
    String responderPergunta(String pergunta);

    @UserMessage("""
        Analise o sentimento do seguinte texto em português e responda APENAS com uma destas palavras:
        POSITIVO, NEUTRO ou NEGATIVO.
        
        Texto: "{texto}"
        """)
    String analisarSentimento(String texto);

    @UserMessage("""
        Classifique a seguinte pergunta em uma destas categorias:
        AGENDAMENTO, ORIENTACAO_PRE_CONSULTA, CONTATO, HORARIO_FUNCIONAMENTO, 
        SIMULACAO_TELECONSULTA, INFORMACAO_GERAL, OUTROS
        
        Pergunta: "{pergunta}"
        Responda APENAS com o nome da categoria:
        """)
    String categorizarPergunta(String pergunta);
}