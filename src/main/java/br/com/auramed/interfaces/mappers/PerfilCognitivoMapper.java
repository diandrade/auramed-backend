package br.com.auramed.interfaces.mappers;

import br.com.auramed.domain.model.PerfilCognitivo;
import br.com.auramed.interfaces.dto.request.PerfilCognitivoRequestDTO;
import br.com.auramed.interfaces.dto.response.PerfilCognitivoResponseDTO;
import java.util.List;

public interface PerfilCognitivoMapper {
    PerfilCognitivo toDomain(PerfilCognitivoRequestDTO dto);
    PerfilCognitivoResponseDTO toResponseDTO(PerfilCognitivo perfilCognitivo);
    List<PerfilCognitivoResponseDTO> toResponseDTOList(List<PerfilCognitivo> perfilCognitivos);
}