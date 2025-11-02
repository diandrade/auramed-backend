package br.com.auramed.interfaces.controllers;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Medico;
import br.com.auramed.domain.service.MedicoService;
import br.com.auramed.interfaces.dto.request.MedicoRequestDTO;
import br.com.auramed.interfaces.dto.response.MedicoResponseDTO;
import br.com.auramed.interfaces.mappers.MedicoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MedicoControllerImpl implements MedicoController {
    private final MedicoService medicoService;
    private final MedicoMapper medicoMapper;

    @Inject
    public MedicoControllerImpl(MedicoService medicoService, MedicoMapper medicoMapper) {
        this.medicoService = medicoService;
        this.medicoMapper = medicoMapper;
    }

    @Override
    public MedicoResponseDTO criarMedico(MedicoRequestDTO medicoRequest) throws EntidadeNaoLocalizadaException {
        try {
            Medico medico = medicoMapper.toDomain(medicoRequest);
            Medico medicoCriado = this.medicoService.criar(medico);
            return medicoMapper.toResponseDTO(medicoCriado);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MedicoResponseDTO editarMedico(Integer id, MedicoRequestDTO medicoRequest) throws EntidadeNaoLocalizadaException {
        Medico medicoExistente = this.medicoService.localizar(id);

        medicoExistente.setCrm(medicoRequest.getCrm());
        medicoExistente.setAceitaTeleconsulta(medicoRequest.getAceitaTeleconsulta());

        if (medicoRequest.getPessoa() != null && medicoExistente.getPessoa() != null) {
            medicoExistente.getPessoa().setNome(medicoRequest.getPessoa().getNome());
            medicoExistente.getPessoa().setEmail(medicoRequest.getPessoa().getEmail());
            medicoExistente.getPessoa().setCpf(medicoRequest.getPessoa().getCpf());
            medicoExistente.getPessoa().setDataNascimento(medicoRequest.getPessoa().getDataNascimento());
            medicoExistente.getPessoa().setGenero(medicoRequest.getPessoa().getGenero());
            medicoExistente.getPessoa().setTelefone(medicoRequest.getPessoa().getTelefone());
        }

        Medico medicoAtualizado = this.medicoService.editar(id, medicoExistente);
        return medicoMapper.toResponseDTO(medicoAtualizado);
    }

    @Override
    public MedicoResponseDTO getMedicoById(Integer id) throws EntidadeNaoLocalizadaException {
        Medico medico = this.medicoService.localizar(id);
        return medicoMapper.toResponseDTO(medico);
    }

    @Override
    public MedicoResponseDTO getMedicoByCrm(String crm) throws EntidadeNaoLocalizadaException {
        Medico medico = this.medicoService.localizarPorCrm(crm);
        return medicoMapper.toResponseDTO(medico);
    }

    @Override
    public void deleteMedico(Integer id) throws EntidadeNaoLocalizadaException {
        this.medicoService.remover(id);
    }

    @Override
    public List<MedicoResponseDTO> getAllMedicos() {
        List<Medico> medicos = medicoService.listarTodos();
        return medicoMapper.toResponseDTOList(medicos);
    }

    @Override
    public List<MedicoResponseDTO> getMedicosPorEspecialidade(Integer idEspecialidade) {
        List<Medico> medicos = medicoService.listarPorEspecialidade(idEspecialidade);
        return medicoMapper.toResponseDTOList(medicos);
    }

    @Override
    public MedicoResponseDTO alterarStatusTeleconsulta(Integer id, String aceitaTeleconsulta) throws EntidadeNaoLocalizadaException {
        Medico medicoAtualizado = this.medicoService.alterarStatusTeleconsulta(id, aceitaTeleconsulta);
        return medicoMapper.toResponseDTO(medicoAtualizado);
    }
}