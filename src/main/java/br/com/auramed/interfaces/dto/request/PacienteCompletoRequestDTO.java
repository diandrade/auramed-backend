package br.com.auramed.interfaces.dto.request;

public class PacienteCompletoRequestDTO {
    private PessoaRequestDTO pessoa;
    private PacienteRequestDTO paciente;
    private InfoTeleconsultaRequestDTO infoTeleconsulta;
    private PerfilCognitivoRequestDTO perfilCognitivo;

    public PacienteCompletoRequestDTO() {}

    public PessoaRequestDTO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaRequestDTO pessoa) {
        this.pessoa = pessoa;
    }

    public PacienteRequestDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteRequestDTO paciente) {
        this.paciente = paciente;
    }

    public InfoTeleconsultaRequestDTO getInfoTeleconsulta() {
        return infoTeleconsulta;
    }

    public void setInfoTeleconsulta(InfoTeleconsultaRequestDTO infoTeleconsulta) {
        this.infoTeleconsulta = infoTeleconsulta;
    }

    public PerfilCognitivoRequestDTO getPerfilCognitivo() {
        return perfilCognitivo;
    }

    public void setPerfilCognitivo(PerfilCognitivoRequestDTO perfilCognitivo) {
        this.perfilCognitivo = perfilCognitivo;
    }
}