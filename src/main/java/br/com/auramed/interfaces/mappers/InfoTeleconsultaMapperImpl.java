package br.com.auramed.interfaces.mappers;

import br.com.auramed.domain.model.InfoTeleconsulta;
import br.com.auramed.interfaces.dto.request.InfoTeleconsultaRequestDTO;
import br.com.auramed.interfaces.dto.response.InfoTeleconsultaResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class InfoTeleconsultaMapperImpl implements InfoTeleconsultaMapper {

    @Override
    public InfoTeleconsulta toDomain(InfoTeleconsultaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        InfoTeleconsulta infoTeleconsulta = new InfoTeleconsulta();
        infoTeleconsulta.setCdHabilidadeDigital(dto.getCdHabilidadeDigital());
        infoTeleconsulta.setCdCanalLembrete(dto.getCdCanalLembrete());
        infoTeleconsulta.setInPrecisaCuidador(dto.getInPrecisaCuidador());
        infoTeleconsulta.setInJaFezTele(dto.getInJaFezTele());
        infoTeleconsulta.setDataCadastro(LocalDateTime.now());
        infoTeleconsulta.setDataAtualizacao(LocalDateTime.now());

        return infoTeleconsulta;
    }

    @Override
    public InfoTeleconsultaResponseDTO toResponseDTO(InfoTeleconsulta infoTeleconsulta) {
        if (infoTeleconsulta == null) {
            return null;
        }

        InfoTeleconsultaResponseDTO dto = new InfoTeleconsultaResponseDTO();
        dto.setIdInfoTeleconsulta(infoTeleconsulta.getIdInfoTeleconsulta());
        dto.setIdPaciente(infoTeleconsulta.getIdPaciente());
        dto.setCdHabilidadeDigital(infoTeleconsulta.getCdHabilidadeDigital());
        dto.setCdCanalLembrete(infoTeleconsulta.getCdCanalLembrete());
        dto.setInPrecisaCuidador(infoTeleconsulta.getInPrecisaCuidador());
        dto.setInJaFezTele(infoTeleconsulta.getInJaFezTele());
        dto.setDataCadastro(infoTeleconsulta.getDataCadastro());
        dto.setDataAtualizacao(infoTeleconsulta.getDataAtualizacao());

        return dto;
    }

    @Override
    public List<InfoTeleconsultaResponseDTO> toResponseDTOList(List<InfoTeleconsulta> infoTeleconsultas) {
        if (infoTeleconsultas == null) {
            return null;
        }

        return infoTeleconsultas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}