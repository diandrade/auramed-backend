package br.com.auramed.interfaces.dto.request;

public class PacienteRequestDTO {
    private Integer idMedicoResponsavel;
    private String nrCartaoSUS;

    public PacienteRequestDTO() {}

    public Integer getIdMedicoResponsavel() {
        return idMedicoResponsavel;
    }

    public void setIdMedicoResponsavel(Integer idMedicoResponsavel) {
        this.idMedicoResponsavel = idMedicoResponsavel;
    }

    public String getNrCartaoSUS() {
        return nrCartaoSUS;
    }

    public void setNrCartaoSUS(String nrCartaoSUS) {
        this.nrCartaoSUS = nrCartaoSUS;
    }
}