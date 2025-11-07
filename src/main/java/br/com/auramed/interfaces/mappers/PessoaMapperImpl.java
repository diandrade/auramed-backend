package br.com.auramed.interfaces.mappers;

import br.com.auramed.domain.model.Pessoa;
import br.com.auramed.interfaces.dto.request.PessoaRequestDTO;
import br.com.auramed.interfaces.dto.response.PessoaResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PessoaMapperImpl implements PessoaMapper {

    @Override
    public Pessoa toDomain(PessoaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Pessoa pessoa = new Pessoa(dto.getNome(), dto.getTelefone(), dto.getTipoPessoa());
        pessoa.setEmail(dto.getEmail());
        pessoa.setCpf(dto.getCpf());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setGenero(dto.getGenero());

        return pessoa;
    }

    @Override
    public PessoaResponseDTO toResponseDTO(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        PessoaResponseDTO dto = new PessoaResponseDTO();
        dto.setId(pessoa.getId());
        dto.setNome(pessoa.getNome());
        dto.setEmail(pessoa.getEmail());
        dto.setCpf(pessoa.getCpf());
        dto.setDataNascimento(pessoa.getDataNascimento());
        dto.setGenero(pessoa.getGenero());
        dto.setTelefone(pessoa.getTelefone());
        dto.setTipoPessoa(pessoa.getTipoPessoa());
        dto.setDataCadastro(pessoa.getDataCadastro());
        dto.setAtivo(pessoa.getAtivo());

        return dto;
    }

    @Override
    public List<PessoaResponseDTO> toResponseDTOList(List<Pessoa> pessoas) {
        if (pessoas == null) {
            return null;
        }

        return pessoas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}