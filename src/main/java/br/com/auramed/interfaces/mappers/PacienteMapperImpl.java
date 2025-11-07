package br.com.auramed.interfaces.mappers;

import br.com.auramed.domain.model.Paciente;
import br.com.auramed.interfaces.dto.request.PacienteRequestDTO;
import br.com.auramed.interfaces.dto.response.PacienteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PacienteMapperImpl implements PacienteMapper {

    @Override
    public Paciente toDomain(PacienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Paciente(null, dto.getIdMedicoResponsavel(), dto.getNrCartaoSUS());
    }

    @Override
    public PacienteResponseDTO toResponseDTO(Paciente paciente) {
        if (paciente == null) {
            return null;
        }

        PacienteResponseDTO dto = new PacienteResponseDTO();
        dto.setIdPessoa(paciente.getIdPessoa());
        dto.setIdMedicoResponsavel(paciente.getIdMedicoResponsavel());
        dto.setNrCartaoSUS(paciente.getNrCartaoSUS());
        dto.setDataCadastro(paciente.getDataCadastro());
        dto.setAtivo(paciente.getAtivo());

        return dto;
    }

    @Override
    public List<PacienteResponseDTO> toResponseDTOList(List<Paciente> pacientes) {
        if (pacientes == null) {
            return null;
        }

        return pacientes.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}