package br.com.auramed.interfaces.mappers;

import br.com.auramed.domain.model.Medico;
import br.com.auramed.domain.model.Pessoa;
import br.com.auramed.interfaces.dto.request.MedicoRequestDTO;
import br.com.auramed.interfaces.dto.response.MedicoResponseDTO;
import br.com.auramed.interfaces.dto.response.PessoaResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MedicoMapperImpl implements MedicoMapper {

    @Inject
    PessoaMapper pessoaMapper;

    @Override
    public Medico toDomain(MedicoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Pessoa pessoa = pessoaMapper.toDomain(dto.getPessoa());
        Medico medico = new Medico(pessoa, dto.getCrm());

        if (dto.getId() != null) {
            medico.setId(dto.getId());
        } else {
            medico.setId(0);
        }

        medico.setAceitaTeleconsulta(dto.getAceitaTeleconsulta());

        return medico;
    }

    @Override
    public MedicoResponseDTO toResponseDTO(Medico medico) {
        if (medico == null) {
            return null;
        }

        MedicoResponseDTO response = new MedicoResponseDTO();
        response.setId(medico.getId());

        if (medico.getPessoa() != null) {
            PessoaResponseDTO pessoaResponse = pessoaMapper.toResponseDTO(medico.getPessoa());
            response.setPessoa(pessoaResponse);
            response.setDataCadastro(medico.getPessoa().getDataCadastro());
        } else {
            PessoaResponseDTO pessoaVazia = new PessoaResponseDTO();
            pessoaVazia.setId(medico.getId());
            response.setPessoa(pessoaVazia);
        }

        response.setCrm(medico.getCrm());
        response.setAceitaTeleconsulta(medico.getAceitaTeleconsulta());

        return response;
    }

    @Override
    public List<MedicoResponseDTO> toResponseDTOList(List<Medico> medicos) {
        if (medicos == null) {
            return List.of();
        }
        return medicos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}