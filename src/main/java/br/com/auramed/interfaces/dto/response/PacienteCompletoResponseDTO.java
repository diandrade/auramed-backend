package br.com.auramed.interfaces.dto.response;

public class PacienteCompletoResponseDTO {
    private PessoaResponseDTO pessoa;
    private PacienteResponseDTO paciente;
    private InfoTeleconsultaResponseDTO infoTeleconsulta;
    private PerfilCognitivoResponseDTO perfilCognitivo;

    public PacienteCompletoResponseDTO() {}

    public PacienteCompletoResponseDTO(PessoaResponseDTO pessoa, PacienteResponseDTO paciente,
                                       InfoTeleconsultaResponseDTO infoTeleconsulta, PerfilCognitivoResponseDTO perfilCognitivo) {
        this.pessoa = pessoa;
        this.paciente = paciente;
        this.infoTeleconsulta = infoTeleconsulta;
        this.perfilCognitivo = perfilCognitivo;
    }

    public PessoaResponseDTO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaResponseDTO pessoa) {
        this.pessoa = pessoa;
    }

    public PacienteResponseDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteResponseDTO paciente) {
        this.paciente = paciente;
    }

    public InfoTeleconsultaResponseDTO getInfoTeleconsulta() {
        return infoTeleconsulta;
    }

    public void setInfoTeleconsulta(InfoTeleconsultaResponseDTO infoTeleconsulta) {
        this.infoTeleconsulta = infoTeleconsulta;
    }

    public PerfilCognitivoResponseDTO getPerfilCognitivo() {
        return perfilCognitivo;
    }

    public void setPerfilCognitivo(PerfilCognitivoResponseDTO perfilCognitivo) {
        this.perfilCognitivo = perfilCognitivo;
    }
}