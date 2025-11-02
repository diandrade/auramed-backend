package br.com.auramed.interfaces.mappers;

import br.com.auramed.domain.model.Paciente;
import br.com.auramed.interfaces.dto.request.PacienteRequestDTO;
import br.com.auramed.interfaces.dto.response.PacienteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PacienteMapperImpl implements PacienteMapper {

    @Inject
    Logger logger;

    @Override
    public Paciente toDomain(PacienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Paciente paciente = new Paciente(
                dto.getIdPessoa(),
                null, // Será definido pelo controller
                dto.getNrCartaoSUS()
        );

        logger.debug("🔧 Paciente mapeado - ID Pessoa: " + dto.getIdPessoa() +
                " | Médico: (será definido pelo contexto)");

        return paciente;
    }

    @Override
    public PacienteResponseDTO toResponseDTO(Paciente paciente) {
        if (paciente == null) {
            return null;
        }

        PacienteResponseDTO response = new PacienteResponseDTO();
        response.setIdPessoa(paciente.getIdPessoa());
        response.setIdMedicoResponsavel(paciente.getIdMedicoResponsavel());
        response.setNrCartaoSUS(paciente.getNrCartaoSUS());
        response.setDataCadastro(paciente.getDataCadastro());
        response.setAtivo(paciente.getAtivo());

        return response;
    }

    @Override
    public List<PacienteResponseDTO> toResponseDTOList(List<Paciente> pacientes) {
        if (pacientes == null) {
            return List.of();
        }
        return pacientes.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}