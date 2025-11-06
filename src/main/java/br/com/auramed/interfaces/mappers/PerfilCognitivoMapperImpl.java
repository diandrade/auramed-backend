package br.com.auramed.interfaces.mappers;

import br.com.auramed.domain.model.PerfilCognitivo;
import br.com.auramed.interfaces.dto.request.PerfilCognitivoRequestDTO;
import br.com.auramed.interfaces.dto.response.PerfilCognitivoResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PerfilCognitivoMapperImpl implements PerfilCognitivoMapper {

    @Override
    public PerfilCognitivo toDomain(PerfilCognitivoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        PerfilCognitivo perfilCognitivo = new PerfilCognitivo();
        perfilCognitivo.setInDificuldadeVisao(dto.getInDificuldadeVisao());
        perfilCognitivo.setInUsaOculos(dto.getInUsaOculos());
        perfilCognitivo.setInDificuldadeAudicao(dto.getInDificuldadeAudicao());
        perfilCognitivo.setInUsaAparelhoAud(dto.getInUsaAparelhoAud());
        perfilCognitivo.setInDificuldadeCogn(dto.getInDificuldadeCogn());
        perfilCognitivo.setDataCadastro(LocalDateTime.now());
        perfilCognitivo.setDataAtualizacao(LocalDateTime.now());

        return perfilCognitivo;
    }

    @Override
    public PerfilCognitivoResponseDTO toResponseDTO(PerfilCognitivo perfilCognitivo) {
        if (perfilCognitivo == null) {
            return null;
        }

        PerfilCognitivoResponseDTO dto = new PerfilCognitivoResponseDTO();
        dto.setIdPerfilCognitivo(perfilCognitivo.getIdPerfilCognitivo());
        dto.setIdPaciente(perfilCognitivo.getIdPaciente());
        dto.setInDificuldadeVisao(perfilCognitivo.getInDificuldadeVisao());
        dto.setInUsaOculos(perfilCognitivo.getInUsaOculos());
        dto.setInDificuldadeAudicao(perfilCognitivo.getInDificuldadeAudicao());
        dto.setInUsaAparelhoAud(perfilCognitivo.getInUsaAparelhoAud());
        dto.setInDificuldadeCogn(perfilCognitivo.getInDificuldadeCogn());
        dto.setDataCadastro(perfilCognitivo.getDataCadastro());
        dto.setDataAtualizacao(perfilCognitivo.getDataAtualizacao());

        return dto;
    }

    @Override
    public List<PerfilCognitivoResponseDTO> toResponseDTOList(List<PerfilCognitivo> perfilCognitivos) {
        if (perfilCognitivos == null) {
            return null;
        }

        return perfilCognitivos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}